package com.github.davishi25.statcheck.features

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.event.GameStartEvent
import com.github.davishi25.statcheck.utils.LocationUtils
import com.github.davishi25.statcheck.utils.StatUtils
import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.mc
import com.github.davishi25.statcheck.utils.Utils.user
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object AutoStatCheck {
    @SubscribeEvent
    fun onGameStart(event: GameStartEvent) {
        val tablist = mc.netHandler.playerInfoMap
        val names = mutableSetOf<String>()
        tablist.forEach { playerInfo ->
            val name = playerInfo.gameProfile.name
            if (name == user.name) return@forEach
            // NPCs do not have a team
            playerInfo?.playerTeam?.teamName ?: return@forEach
            names.add(name)
        }

        if (Config.debugMode) Utils.modMessage("Detected names: $names")

        names.forEach { name ->
            try {
                if(LocationUtils.currentMode.equals("DUELS")) {
                    StatUtils.createDuelsStatMessage(name, LocationUtils.currentDuelsMode!!)
                } else {
                    StatUtils.createMinimalStatMessage(name, LocationUtils.currentMode!!.lowercase())
                }
            } catch (e: Exception) {
                Utils.modMessage("Encountered Error on name $name: ${e.message}")
            }
        }
    }
}