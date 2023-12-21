package top.ffshaozi

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.event.subscribeGroupMessages
import net.mamoe.mirai.utils.info
import api.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.console.plugin.id
import net.mamoe.mirai.event.events.BotOnlineEvent
import top.ffshaozi.command.SettingCommand
import top.ffshaozi.config.Bindings
import top.ffshaozi.config.Setting
import java.text.SimpleDateFormat
import java.util.Date

object BF1ServerManager : KotlinPlugin(
    JvmPluginDescription(
        id = "top.ffshaozi.BF1ServerManager",
        name = "BF1ServerManager",
        version = "1.1.0",
    ) {
        author("LittleArray")
    }
) {
    override fun onEnable() {
        Bindings.reload()
        Setting.reload()
        SettingCommand.register()
        globalEventChannel().subscribeAlways<BotOnlineEvent>{ bot ->
            bot.bot.groups.forEach { event ->
                //LRC通知
                Bindings.bindingServer[event.id]?.forEach {
                    var oldLrc : Api.LRCLog = Api.LRCLog("",0,false,"")
                    val api = Api(event.id, 10086)
                    api.gameID = it.gameid
                    api.token = it.token
                    CoroutineScope(Dispatchers.IO).launch {
                        while (bot.bot.isOnline){
                            api.getLRClogs()?.let { new ->
                                if (oldLrc.id != new.id && System.currentTimeMillis() - new.time < 5 * 60 * 1000){
                                    event.sendMessage("在服务器 ${Bindings.bindingServer[event.id]?.find { server -> server.gameid == api.gameID }?.name} 中玩家 ${new.id} 数据异常提醒\n\n ${new.msg.replace("&&","\n ")}\n${if(new.kick) "已踢出该玩家" else "未踢出该玩家"} 更新时间:${SimpleDateFormat("MM-dd HH:mm:ss").format(new.time)}\nTips:目前版本无法判断刷枪和双伤导致的数据异常,如果出现误判请联系管理使用 awl 服务器名 误判ID 解除")
                                    delay(500)
                                }
                                oldLrc = new
                            }
                            delay(1000 * 30)
                        }
                    }
                }
            }
        }
        globalEventChannel().subscribeGroupMessages {
            sentByOperator() quoteReply {
                val event = this
                val api = Api(event.group.id, event.sender.id)

                val cmd = it.split(" ")
                //logger.info { cmd.toString() }
                if (cmd.getOrNull(0) == "shelp")
                    """
                            管服帮助 v1.1
                            第二个参数一定是服务器名
                            kick 服务器名 ID 理由 踢出CD -踢人
                            ban 服务器名 ID -实体封禁玩家
                            vban 服务器名 ID -虚拟封禁
                            rvban 服务器名 ID -移除虚拟封禁
                            rban 服务器名 ID -移除实体封禁
                            av 服务器名 ID 时长? -添加vip,时长为天
                            rv 服务器名 ID -移除vip,时长为天
                            awl 服务器名 ID 时长? -添加白名单,时长为天
                            rwl 服务器名 ID -移除白名单,时长为天
                            qt 服务器名 地图名 -切图
                            boom 服务器名 理由 -炸服
                            move 服务器名 ID -换边
                            maps 服务器名 -查看图池
                            restart 服务器名 -重开
                            +alias 服务器名 原名 别名 -添加别名
                            -alias 服务器名 别名 -添加别名
                        """.trimIndent()
                else
                    cmd.getOrNull(1)?.let {
                        Bindings.bindingServer[event.group.id]?.find { server -> server.name == it }?.let {
                            api.gameID = it.gameid
                            api.token = it.token
                            //命令解析
                            commandProcessor(cmd, api)
                        }
                    }
            }
        }
        logger.info { "Plugin loaded" }
    }

    override fun onDisable() {
        super.onDisable()
        SettingCommand.unregister()
    }

    private fun commandProcessor(cmd: List<String>, api: Api, alias: String? = null): Any? {
        cmd.getOrNull(0)?.let { it ->
            val cmd0 = alias ?: it
            return when (cmd0) {
                "kick" -> {
                    val id = cmd.getOrNull(2)
                    val reason = cmd.getOrNull(3)
                    val cd = try {
                        cmd.getOrNull(4)?.toInt()
                    } catch (e: Exception) {
                        0
                    }
                    id?.let {
                        api.kick(id, reason, cd)
                    }
                }

                "qt" -> {
                    val mapName = cmd.getOrNull(2)
                    mapName?.let {
                        api.chooseMap(mapName)
                    }
                }

                "move" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.move(it)
                    }
                }

                "boom" -> {
                    val reason = cmd.getOrNull(2)
                    reason?.let {
                        api.boom(reason)
                    }
                }

                "maps" -> {
                    val maps = api.getMaps()
                    "当前地图:\n${maps?.nowMap}\n地图池:\n${maps?.maps}"
                }

                "restart" -> {
                    val nowMap = api.getMaps()?.nowMap
                    nowMap?.let {
                        api.chooseMap(nowMap)
                    }
                }

                "ban" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.ban(id)
                    }
                }

                "rban" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.rban(id)
                    }
                }

                "vban" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.vban(id)
                    }
                }

                "rvban" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.rvban(id)
                    }
                }

                "av" -> {
                    val id = cmd.getOrNull(2)
                    val time = cmd.getOrNull(3)
                    id?.let {
                        api.aVip(id, time)
                    }
                }

                "rv" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.rVip(id)
                    }
                }

                "awl" -> {
                    val id = cmd.getOrNull(2)
                    val time = cmd.getOrNull(3)
                    id?.let {
                        api.awl(id, time)
                    }
                }

                "rwl" -> {
                    val id = cmd.getOrNull(2)
                    id?.let {
                        api.rwl(id)
                    }
                }

                "+alias" -> {
                    val orName = cmd.getOrNull(2)
                    val alName = cmd.getOrNull(3)
                    orName?.let {
                        alName?.let {
                            addAlias(orName, alName, api.opGroup)
                            "添加别名成功"
                        }
                    }
                }

                "-alias" -> {
                    val alName = cmd.getOrNull(2)
                    alName?.let {
                        rmAlias(alName, api.opGroup)?.let {
                            "移除别名成功 $alName $it"
                        }
                    }
                }

                else -> {
                    if (alias == null && cmd.getOrNull(0) != "null")
                        commandProcessor(listOf("null"), api, getAlias(cmd0, api.opGroup))
                    else
                        null
                }
            }
        }
        return null
    }

    private fun addAlias(orName: String, alName: String, groupId: Long) {
        val aliasMap = Bindings.alias[groupId] ?: mutableMapOf()
        aliasMap[alName] = orName
        Bindings.alias[groupId] = aliasMap
    }

    private fun rmAlias(alName: String, groupId: Long): String? {
        return Bindings.alias[groupId]?.remove(alName)
    }

    private fun getAlias(alName: String, groupId: Long): String? {
        return Bindings.alias[groupId]?.get(alName)
    }
}