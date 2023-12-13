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
    suspend fun CommandSender.adds(groupid: Long? = this.subject?.id,gameid: String, name: String, token: String) {
        if (groupid == null) return
        Bindings.addServer(gameid, groupid, name, token)
        sendMessage("群组绑定列表 ${Bindings.bindingServer[groupid]}")
    }

}