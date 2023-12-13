package top.ffshaozi.config

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.ValueDescription
import net.mamoe.mirai.console.data.value
import top.ffshaozi.config.Bindings.provideDelegate

/**
 * @Description
 * @Author littleArray
 * @Date 2023/11/14
 */
object Setting : AutoSavePluginData("Setting"){
    @ValueDescription("查询服务器地址")
    var serverUrl by value("http://ipv6.ffshaozi.top:8080")
    @ValueDescription("管服服务器地址")
    var opServerUrl by value("http://ipv6.ffshaozi.top:2086")
}