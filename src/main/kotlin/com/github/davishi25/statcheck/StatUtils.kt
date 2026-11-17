package com.github.davishi25.statcheck

import com.github.davishi25.statcheck.parser.ApiParser
import com.github.davishi25.statcheck.parser.BridgeDuelsParser
import com.github.davishi25.statcheck.parser.DuelsParser
import com.google.gson.JsonObject
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
            val statLine = checkStats(apiResponse.getAsJsonObject("stats"), parser)

            Utils.chat("$padding\n§8| $nameLine§8| $statLine\n$padding")
        } catch (e: Exception) {
            Utils.modMessage(e.message)
        }
    }

    fun checkStats(playerObj: JsonObject, parser: ApiParser): String = parser.getStatLine(playerObj)

    fun createDuelsStatMessage(player: String, duelsMode: String) = scope.launch {
        try {
            val apiResponse = API.getAPI(player)
            val parser = if (duelsMode == "bridge") {
                ApiParserRegistry.parsers["bridge"]!!
            } else {
                ApiParserRegistry.parsers["duels"]!!
            }

            val padding = "§8----------------------------------------"
            val nameLine = Utils.getFormattedName(apiResponse) + "'s " + duelsMode + " duels§r\n"
            val statLine = checkDuelsStats(apiResponse.getAsJsonObject("stats"), parser, duelsMode)

            Utils.chat("$padding\n§8| $nameLine§8| $statLine\n$padding")
        } catch (e: Exception) {
            Utils.modMessage(e.message)
        }
    }

    fun checkDuelsStats(playerObj: JsonObject, parser: ApiParser, gamemode: String): String {
        if (parser is BridgeDuelsParser)
            return parser.getStatLine(playerObj)
        return (parser as DuelsParser).getStatLine(gamemode, playerObj)
    }
}