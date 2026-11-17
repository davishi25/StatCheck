package com.github.davishi25.statcheck.parser

import com.github.davishi25.statcheck.Utils
import com.google.gson.JsonObject

object BedwarsParser: ApiParser {
    data class BedwarsContext(private val bedwarsObj: JsonObject) {

        fun getWins() = bedwarsObj.get("wins_bedwars")?.asInt ?: 0
        fun getFinals() = bedwarsObj.get("final_kills_bedwars")?.asInt ?: 0
        fun getFinalDeaths() = bedwarsObj.get("final_deaths_bedwars")?.asInt ?: 0
        fun getFKDR(): Double {
            val deaths = getFinalDeaths()
            return getFinals().toDouble() / (if(deaths == 0) 1 else deaths).toDouble()
        }
    }

    override fun getStatLine(playerObj: JsonObject): String {
        val ctx = BedwarsContext(playerObj.getAsJsonObject("Bedwars"))
        return "§rW: §a" + ctx.getWins() + "§r | Finals: " + ctx.getFinals() + " | FKDR: " + Utils.roundToPrecision(ctx.getFKDR(),2)
    }

    override fun toString() = "Bedwars"
}
