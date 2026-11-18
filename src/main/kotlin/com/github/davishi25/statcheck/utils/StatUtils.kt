package com.github.davishi25.statcheck.utils

import com.github.davishi25.statcheck.API
import com.github.davishi25.statcheck.ApiParserRegistry
import com.github.davishi25.statcheck.parser.ApiParser
import com.github.davishi25.statcheck.parser.BridgeDuelsParser
import com.github.davishi25.statcheck.parser.DuelsParser
import com.github.davishi25.statcheck.playerdata.StatsData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object StatUtils {
    private val scope = CoroutineScope(Dispatchers.IO)
    fun createStatMessage(player: String, game: String) = scope.launch {
        try {
            val apiResponse = API.getAPI(player)
            val parser = ApiParserRegistry.parsers[game]
                ?: throw NullPointerException("Couldn't find gamemode \"$game\". Did you spell it correctly?")
            val padding = "§8----------------------------------------"
            val nameLine = Utils.getNameLine(apiResponse, parser)
            val statLine = checkStats(apiResponse.stats, parser)

            Utils.chat("$padding\n§8| $nameLine§8| $statLine\n$padding")
        } catch (e: Exception) {
            Utils.modMessage(e.message)
        }
    }

    fun createMinimalStatMessage(player: String, game: String) = scope.launch {
        try {
            val playerData = API.getAPI(player)
            val parser = ApiParserRegistry.parsers[game]
                ?: throw NullPointerException("Couldn't find gamemode \"$game\". Did you spell it correctly?")
            val formattedName = Utils.getFormattedName(playerData)
            val statLine = checkVitalStats(playerData.stats, parser)

            Utils.chat("$formattedName §8| $statLine")
        } catch (e: Exception) {
            Utils.modMessage(e.message)
        }
    }

    fun checkStats(statsData: StatsData?, parser: ApiParser): String = parser.getStatLine(statsData)
    fun checkVitalStats(statsData: StatsData?, parser: ApiParser): String = parser.getVitalStatLine(statsData)

    fun createDuelsStatMessage(player: String, duelsMode: String?) = scope.launch {
        try {
            val playerData = API.getAPI(player)
            val parser = if (duelsMode == "bridge") {
                ApiParserRegistry.parsers["bridge"]!!
            } else {
                ApiParserRegistry.parsers["duels"]!!
            }

            val padding = "§8----------------------------------------"
            val nameLine = Utils.getFormattedName(playerData) + "'s " + duelsMode + " duels§r\n"
            val statLine =
                checkDuelsStats(playerData.stats ?: throw Exception("Could not get stats data!"), parser, duelsMode)

            Utils.chat("$padding\n§8| $nameLine§8| $statLine\n$padding")
        } catch (e: Exception) {
            Utils.modMessage(e.message)
        }
    }

    fun checkDuelsStats(statsData: StatsData, parser: ApiParser, gamemode: String?): String {
        if (parser is BridgeDuelsParser)
            return parser.getStatLine(statsData)
        return (parser as DuelsParser).getStatLine(gamemode, statsData)
    }
}