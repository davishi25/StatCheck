package com.github.davishi25.statcheck.api.parser

import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.roundToPrecision
import com.github.davishi25.statcheck.api.playerdata.StatsData
import com.github.davishi25.statcheck.api.playerdata.stats.BedwarsData

object BedwarsParser: ApiParser {
    data class BedwarsContext(private val bedwarsData: BedwarsData) {
        fun getWins() = bedwarsData.wins_bedwars ?: 0
        fun getFinals() = bedwarsData.final_kills_bedwars ?: 0
        fun getFinalDeaths() = bedwarsData.final_deaths_bedwars ?: 0

        fun getFKDR(): Double {
            val deaths = getFinalDeaths()
            return getFinals().toDouble() / (if(deaths == 0) 1 else deaths).toDouble()
        }

        fun getStar(): Int {
            val exp = bedwarsData.Experience ?: 0
            if (exp <= 0) return 0

            val xpPerPrestige = 487_000L

            val prestiges = (exp / xpPerPrestige).toInt()
            var remainingXp = exp % xpPerPrestige
            var level = prestiges * 100

            val easyLevelsXp = longArrayOf(500L, 1000L, 2000L, 3500L)
            easyLevelsXp.forEach { xpRequired ->
                if (remainingXp >= xpRequired) {
                    remainingXp -= xpRequired
                    level++
                } else {
                    return level
                }
            }

            val additionalLevels = (remainingXp / 5000L).toInt()
            level += additionalLevels

            return level
        }
    }

    override fun getStatLine(statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = BedwarsContext(statsData.bedwars)
        val star = ctx.getStar()
        val wins = ctx.getWins()
        val finals = ctx.getFinals()
        val FKDR = ctx.getFKDR().roundToPrecision(2)
        return "§r§6Star: §r$star §8| §r§aWins: §r$wins §8| §r§9Finals: §r$finals §8| §r§cFKDR: §r$FKDR"
    }
    override fun getVitalStatLine(statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = BedwarsContext(statsData.bedwars)
        val star = ctx.getStar()
        val finals = ctx.getFinals()
        val FKDR = ctx.getFKDR().roundToPrecision(2)
        return "§r§6Star: §r$star §8| §r§9Finals: §r$finals §8| §r§cFKDR: §r$FKDR"
    }

    override fun toString() = "Bedwars"
}
