package top.ffshaozi.config

import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import top.ffshaozi.config.Bindings.provideDelegate

/**
 * @Description
 * @Author littleArray
 * @Date 2023/11/14
 */
object Bindings : AutoSavePluginData("Bindings") {
    @ValueDescription("绑定的服务器")
    var bindingServer: MutableMap<Long, MutableList<Server>> by value()//Long群号
    @ValueDescription("别名")
    var alias: MutableMap<Long, MutableMap<String,String>> by value()//Long群号
    @ValueDescription("通知群群号")
    var notificationGroups:MutableList<Long> by value()
    @Serializable
    data class Server(
        var gameid: String,
        val name: String,
        val token: String
    )

    fun addServer(gameid: String, groupid: Long, name: String, token: String): String {
        val old = bindingServer[groupid] ?: mutableListOf()
        return if (old.none { it.gameid == gameid }) {
            old.add(Server(gameid, name, token))
            bindingServer[groupid] = old
            "绑定成功"
        } else {
            "已绑定过该服务器,请删除后重新绑定"
        }
    }
    fun replaceServer(name: String, newGameid: String): Boolean {
        bindingServer.forEach {
            it.value.forEach {
                if (it.name == name) {
                    it.gameid = newGameid
                    return true
                }
            }
        }
        return false
    }

    fun rmServer(gameid: String, groupid: Long): String {
        return bindingServer[groupid]?.removeIf {
            it.gameid == gameid
        }.toString()
    }

    fun getServer(groupid: Long, name: String): Server? {
        bindingServer[groupid]?.forEach {
            if (it.name == name){
                return it
            }
        }
        return null
    }
}