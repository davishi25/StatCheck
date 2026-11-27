package com.github.davishi25.statcheck.features

import com.github.davishi25.statcheck.api.API
import com.github.davishi25.statcheck.api.ApiParserRegistry
import com.github.davishi25.statcheck.config.Config
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
    private val renderedInfo = mutableMapOf<NetworkPlayerInfo, RenderState>()
    private val cachedTeams = mutableMapOf<String, String>()
    private val teamGames = listOf("BEDWARS")

    @SubscribeEvent
    fun onRenderGameOverlay(event: RenderGameOverlayEvent.Pre) {
        if (!LocationUtils.isOnHypixel) return
        if (!LocationUtils.inGame) return
        if (event.type != RenderGameOverlayEvent.ElementType.PLAYER_LIST) return
        val parser = ApiParserRegistry.getParser(LocationUtils.gametype?.lowercase() ?: return) ?: return

        val presentInfos = HashSet<NetworkPlayerInfo>()
        TablistUtils.tablist.forEach { info ->
            info ?: return@forEach
            val name = info.gameProfile?.name ?: return@forEach
            presentInfos.add(info)
            if (renderedInfo[info] == RenderState.FINISHED) return@forEach
            val teamComponent = getTeam(info) ?: return@forEach
            val nicked = API.isNicked(name)
            if (renderedInfo[info] == RenderState.MISSING_DATA && !nicked && API.getCachedData(name) == null) return@forEach

            if (Config.debugMode) Utils.modMessage("Computing tab entry for $name")

            if (nicked) {
                info.displayName = ChatComponentText("$teamComponent§7$name §8(§cNicked§8)")
                renderedInfo[info] = RenderState.FINISHED
                return@forEach
            }

            val cachedData = API.getCachedData(name)
            if (cachedData == null) {
                info.displayName = ChatComponentText("$teamComponent§7$name §8(§7Fetching...§8)")
                renderedInfo[info] = RenderState.MISSING_DATA
                return@forEach
            }

            val formattedName = Utils.getFormattedName(cachedData)
            val statComponent = StatUtils.checkVitalStats(cachedData.stats, parser)

            val displayText = "$teamComponent$formattedName $statComponent"

            info.displayName = ChatComponentText(displayText)
            renderedInfo[info] = RenderState.FINISHED
        }
        val staleInfos = HashSet<NetworkPlayerInfo>()
        renderedInfo.keys.forEach { info -> if (!presentInfos.contains(info)) staleInfos.add(info) }
        staleInfos.forEach { info -> renderedInfo.remove(info) }
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

    private enum class RenderState { MISSING_DATA, FINISHED }
}