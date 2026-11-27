package com.github.davishi25.statcheck.utils

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.utils.Utils.mc
import com.github.davishi25.statcheck.utils.Utils.user
import net.minecraft.client.network.NetworkPlayerInfo

object TablistUtils {
    val tablist: Collection<NetworkPlayerInfo?> get() = mc.netHandler.playerInfoMap

    fun getNames(includeSelf: Boolean = false): Set<String> {
        val names = mutableSetOf<String>()
        tablist.forEach { playerInfo ->
            playerInfo ?: return@forEach
            val name = playerInfo.gameProfile.name
            if (!includeSelf && name == user.name) return@forEach
            // NPCs do not have a team
            playerInfo.playerTeam?.teamName ?: return@forEach
            names.add(name)
        }
        if (Config.debugMode) Utils.modMessage("Detected names: $names")
        return names
    }

    fun getEntries(): Set<String> {
        val entries = mutableSetOf<String>()
        tablist.forEach { playerInfo ->
            playerInfo ?: return@forEach
            playerInfo.gameProfile?.name?.let { entries.add(it.replace('§','&')) }
        }
        return entries
    }
}