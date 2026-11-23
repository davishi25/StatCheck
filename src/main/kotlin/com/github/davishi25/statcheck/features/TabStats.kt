package com.github.davishi25.statcheck.features

import com.github.davishi25.statcheck.api.API
import com.github.davishi25.statcheck.api.ApiParserRegistry
import com.github.davishi25.statcheck.utils.location.LocationUtils
import com.github.davishi25.statcheck.utils.StatUtils
import com.github.davishi25.statcheck.utils.TablistUtils
import com.github.davishi25.statcheck.utils.Utils
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object TabStats {
    private val renderedInfo = HashSet<NetworkPlayerInfo>()
    private val cachedTeams = mutableMapOf<String, String>()
    private val teamGames = listOf("BEDWARS")

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        if (!LocationUtils.isOnHypixel) return
        if (!LocationUtils.inGame) return
        if (event.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return
        val parser = ApiParserRegistry.getParser(LocationUtils.gametype?.lowercase() ?: return) ?: return

        TablistUtils.tablist.forEach { info ->
            info ?: return@forEach
            if (renderedInfo.contains(info)) return@forEach
            val name = info.gameProfile?.name ?: return@forEach
            val teamComponent = getTeam(info) ?: return@forEach

            val cachedData = API.getCachedData(name)
            val formattedName = if (cachedData != null) {
                Utils.getFormattedName(cachedData)
            } else "§7$name"

            val cachedStats = cachedData?.stats
            val statComponent = if (cachedStats != null) {
                StatUtils.checkVitalStats(cachedStats, parser)
            } else {
                if (API.isNicked(name)) "§8(§cNicked§8)"
                else "§8(§7Fetching...§8)"
            }

            val displayComponent = ChatComponentText("$teamComponent$formattedName")
            info.displayName = displayComponent.appendText(" $statComponent")
            cachedData?.let {
                if (!teamGames.contains(LocationUtils.gametype) || teamComponent.isNotEmpty()) {
                    renderedInfo.add(info)
                }
            }
        }
    }

    @SubscribeEvent
    fun onWorldUnload(event: WorldEvent.Unload) {
        renderedInfo.clear()
        cachedTeams.clear()
    }

    private fun getTeam(info: NetworkPlayerInfo): String? {
        if (!teamGames.contains(LocationUtils.gametype)) return ""
        if (!LocationUtils.gameStarted) return null
        cachedTeams[info.gameProfile.name]?.let { return it }

        val colorPrefix = info.playerTeam?.colorPrefix ?: return null
        if (colorPrefix.isNotEmpty()) cachedTeams[info.gameProfile.name] = colorPrefix

        return colorPrefix
    }
}