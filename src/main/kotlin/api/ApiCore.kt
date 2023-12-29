package top.ffshaozi.api

import com.google.gson.Gson
import net.mamoe.mirai.console.command.CommandContext
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.contact.Contact
import okhttp3.OkHttpClient
import okhttp3.Request
import top.ffshaozi.config.Setting
import top.ffshaozi.data.UserInfo
import java.util.concurrent.TimeUnit


/**
 * @Description
 * @Author littleArray
 * @Date 2023/10/18
 */
object ApiCore {
    data class PostResponse(
        var isSuccessful: Boolean = false,
        var error: String = "",
        var reqBody: String = "",
    )

    val okHttpClient = OkHttpClient()

    //数据Api接口
    fun build(url: String): PostResponse {
        return try {
            val request = Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .build()
            val response = okHttpClient
                .newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//设置读取超时时间
                .build()
                .newCall(request).execute()
            if (response.isSuccessful) {
                PostResponse(isSuccessful = true, reqBody = response.body.string())
            } else {
                PostResponse(isSuccessful = false, reqBody = response.body.string())
            }
        } catch (ex: Exception) {
            PostResponse(isSuccessful = false, error = ex.stackTraceToString())
        }
    }

    fun getUser(userName: String): UserInfo? {
        val response = build("${Setting.serverUrl}/gateway/getUser/$userName")
        if (response.isSuccessful) {
            return try {
                Gson().fromJson(response.reqBody, UserInfo::class.java)
            } catch (e: Exception) {
                null
            }
        }
        return null
    }


    fun postData(
        gameID: String,
        method: String,
        param: String,
        group: Long? = null,
        subjectID: Long? = null,
        token: String
    ): String? {
        val response = try {
            val request = Request.Builder()
                .url("${Setting.opServerUrl}/${gameID}/${method}/${param}")
                .addHeader("qqGroup", "$group")
                .addHeader("qq", "$subjectID")
                .addHeader("token", token)
                .build()
            val response = okHttpClient
                .newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//设置读取超时时间
                .build()
                .newCall(request).execute()
            if (response.isSuccessful) {
                PostResponse(isSuccessful = true, reqBody = response.body.string())
            } else {
                PostResponse(isSuccessful = false, reqBody = response.body.string())
            }
        } catch (ex: Exception) {
            PostResponse(isSuccessful = false, error = ex.stackTraceToString())
        }
        return if (response.isSuccessful) {
            response.reqBody
        } else {
            null
        }
    }
    fun getLRClogs(): String? {
        val response = try {
            val request = Request.Builder()
                .url("${Setting.opServerUrl}/lrcLogs")
                .build()
            val response = okHttpClient
                .newBuilder()
                .connectTimeout(15, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(15, TimeUnit.SECONDS)//设置读取超时时间
                .build()
                .newCall(request).execute()
            if (response.isSuccessful) {
                PostResponse(isSuccessful = true, reqBody = response.body.string())
            } else {
                PostResponse(isSuccessful = false, reqBody = response.body.string())
            }
        } catch (ex: Exception) {
            PostResponse(isSuccessful = false, error = ex.stackTraceToString())
        }
        return if (response.isSuccessful) {
            response.reqBody
        } else {
            null
        }
    }

}