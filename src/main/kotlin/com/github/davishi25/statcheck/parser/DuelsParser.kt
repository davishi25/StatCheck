package com.github.davishi25.statcheck.parser

import com.github.davishi25.statcheck.Utils
import com.google.gson.JsonObject

object DuelsParser: ApiParser {
    open class DuelsContext(private val gamemode: String, private val duelsObj: JsonObject) {
        fun getDuelsObj() = duelsObj

        open fun getWins(): Int {
            if(gamemode.isEmpty()) return duelsObj.get("wins")?.asInt ?: 0
            return getWins("duel") + getWins("doubles") + getWins("threes") +
                    getWins("four")
        }
        fun getWins(mode: String) = duelsObj.get("${gamemode}_${mode}_wins")?.asInt ?: 0

        open fun getLosses(): Int {
            if(gamemode.isEmpty()) return duelsObj.get("losses")?.asInt ?: 0
            return getLosses("duel") + getLosses("doubles") + getLosses("threes") +
                    getLosses("four")
        }
        fun getLosses(mode: String) = duelsObj.get("${gamemode}_${mode}_losses")?.asInt ?: 0

        fun getWLR(): Double {
            val losses = getLosses()
            return getWins().toDouble() / (if(losses == 0) 1 else losses).toDouble()
        }
    }
    override fun getStatLine(playerObj: JsonObject) =  getStatLine("",playerObj)
    fun getStatLine(gamemode: String, playerObj: JsonObject): String {
        val ctx = DuelsContext(gamemode, playerObj.getAsJsonObject("Duels"))
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Utils.roundToPrecision(ctx.getWLR(),2)
    }
    override fun toString() = "Duels"
}
