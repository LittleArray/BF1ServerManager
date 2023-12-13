package api

import com.google.gson.Gson
import top.ffshaozi.api.ApiCore


class Api(
    var opQQ: Long,
    var opGroup: Long,
) {
    lateinit var token: String
    lateinit var gameID: String
    fun kick(
        id: String,
        reason: String? = null,
        kickCD: Int? = null
    ): String? {
        var param = id
        if (reason != null) param += ",${reason}"
        if (kickCD != null) param += ",${kickCD}"
        return ApiCore.postData(
            gameID,
            "obscureKick",
            param,
            opQQ,
            opGroup,
            token
        )
    }
    fun move(
        id: String,
    ): String? {
        return ApiCore.postData(
            gameID,
            "obscureMove",
            id,
            opQQ,
            opGroup,
            token
        )
    }

    data class Maps(
        val maps: List<String> = listOf(),
        val nowMap: String? = null
    )

    fun getMaps(): Maps? {
        val postData = ApiCore.postData(
            gameID,
            "getMaps",
            "no",
            opQQ,
            opGroup,
            token
        ) ?: return null
        return try {
            Gson().fromJson(postData, Maps::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun chooseMap(mapName: String): String? {
        return ApiCore.postData(
            gameID,
            "chooseMap",
            mapName,
            opQQ,
            opGroup,
            token
        )
    }
    fun boom(reason: String): String? {
        return ApiCore.postData(
            gameID,
            "boom",
            reason,
            opQQ,
            opGroup,
            token
        )
    }

    fun ban(id: String): String? {
        return ApiCore.postData(
            gameID,
            "ban",
            id,
            opQQ,
            opGroup,
            token
        )
    }
    fun rban(id: String): String? {
        val pid = ApiCore.getUser(id)?.personas?.persona?.firstOrNull()?.personaId
        return pid?.let {
            ApiCore.postData(
                gameID,
                "rban",
                it.toString(),
                opQQ,
                opGroup,
                token
            )
        }
    }

    fun vban(id: String): String? {
        val pid = ApiCore.getUser(id)?.personas?.persona?.firstOrNull()?.personaId
        return pid?.let {
            ApiCore.postData(
                gameID,
                "vban",
                it.toString(),
                opQQ,
                opGroup,
                token
            )
        }
    }
    fun rvban(id: String): String? {
        val pid = ApiCore.getUser(id)?.personas?.persona?.firstOrNull()?.personaId
        return pid?.let {
            ApiCore.postData(
                gameID,
                "rvban",
                it.toString(),
                opQQ,
                opGroup,
                token
            )
        }
    }

    fun aVip(id: String,time:String?=null): String? {
        val pid = ApiCore.getUser(id)?.personas?.persona?.firstOrNull()?.personaId
        var param = id
        if (pid != null) param += ",${pid}"
        if (time != null) param += ",${time}"
        return ApiCore.postData(
            gameID,
            "aVip",
            param,
            opQQ,
            opGroup,
            token
        )
    }
    fun awl(id: String,time:String?=null): String? {
        var param = id
        if (time != null) param += ",${time}"
        return ApiCore.postData(
            gameID,
            "awl",
            param,
            opQQ,
            opGroup,
            token
        )
    }

    fun rwl(id: String): String? {
        return ApiCore.postData(
            gameID,
            "rwl",
            id,
            opQQ,
            opGroup,
            token
        )
    }

    fun rVip(id: String): String? {
        val pid = ApiCore.getUser(id)?.personas?.persona?.firstOrNull()?.personaId
        return pid?.let {
            ApiCore.postData(
                gameID,
                "rVip",
                pid.toString(),
                opQQ,
                opGroup,
                token
            )
        }
    }
}
