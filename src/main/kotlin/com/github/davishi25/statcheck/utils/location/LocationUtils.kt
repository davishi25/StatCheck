package com.github.davishi25.statcheck.utils.location

import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.mc

object LocationUtils {
    var gametype: String? = null
    var mode: String? = null
    val inGame: Boolean get() = mode != null
    val isOnHypixel: Boolean get() = mc.currentServerData?.serverIP?.contains("hypixel") ?: false

    fun getDuelsGamemode(): String? {
        mode ?: return null
        val gamemode = mode!!.lowercase()
        Utils.duelsModes.forEach { duelMode ->
            if(gamemode.contains(duelMode)) return duelMode
        }
        return null
    }
}