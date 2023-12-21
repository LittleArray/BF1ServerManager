package top.ffshaozi.command

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.descriptor.ExperimentalCommandDescriptors
import top.ffshaozi.BF1ServerManager
import top.ffshaozi.config.Bindings

/**
 * @Description
 * @Author littleArray
 * @Date 2023/10/18
 */
object SettingCommand : CompositeCommand(
    BF1ServerManager,
    primaryName = "bf1ma",
    description = "战地1管服机器人设置"
) {
    @ExperimentalCommandDescriptors
    override val prefixOptional: Boolean = true

    @SubCommand()
    @Description("添加群组")
    suspend fun CommandSender.ag(groupId: Long? = this.subject?.id) {
        if (groupId == null) return
        Bindings.bindingServer[groupId] = mutableListOf()
        sendMessage("添加群组成功 $groupId")
    }

    @SubCommand()
    @Description("移除群组")
    suspend fun CommandSender.rg(groupId: Long? = this.subject?.id) {
        if (groupId == null) return
        Bindings.bindingServer.remove(groupId)
        sendMessage("移除群组成功 $groupId")
    }

    @SubCommand()
    @Description("添加服务器")
    suspend fun CommandSender.add(name: String, gameID: String, token: String, groupID: Long? = this.subject?.id) {
        if (groupID == null) return
        Bindings.addServer(gameID, groupID, name, token)
        sendMessage("群组绑定列表 ${Bindings.bindingServer[groupID]}")
    }
    @SubCommand()
    @Description("移除服务器")
    suspend fun CommandSender.remove(gameID: String, groupID: Long? = this.subject?.id) {
        if (groupID == null) return
        Bindings.rmServer(gameID, groupID)
        sendMessage("群组绑定列表 ${Bindings.bindingServer[groupID]}")
    }

    @SubCommand()
    @Description("修正服务器")
    suspend fun CommandSender.replace(name: String, newGameID: String) {
        Bindings.replaceServer( name, newGameID)
        sendMessage("成功")
    }

}