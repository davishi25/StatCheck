package com.github.davishi25.statcheck.parser

import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.roundToPrecision
import com.github.davishi25.statcheck.playerdata.StatsData
import com.github.davishi25.statcheck.playerdata.stats.BedwarsData

object BedwarsParser: ApiParser {
    data class BedwarsContext(private val bedwarsData: BedwarsData) {
        fun getWins() = bedwarsData.wins_bedwars ?: 0
        fun getFinals() = bedwarsData.final_kills_bedwars ?: 0
        fun getFinalDeaths() = bedwarsData.final_deaths_bedwars ?: 0

        fun getFKDR(): Double {
            val deaths = getFinalDeaths()
            return getFinals().toDouble() / (if(deaths == 0) 1 else deaths).toDouble()
        }
    }

    override fun getStatLine(statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = BedwarsContext(statsData.bedwars)
        return "§rW: §a" + ctx.getWins() + "§r | Finals: " + ctx.getFinals() + " | FKDR: " + ctx.getFKDR().roundToPrecision(2)
    }
    override fun getVitalStatLine(statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = BedwarsContext(statsData.bedwars)
        val finals = ctx.getFinals()
        val FKDR = ctx.getFKDR().roundToPrecision(2)
        return "§rFinals: $finals §8| §rFKDR: $FKDR"
    }

    override fun toString() = "Bedwars"
}
