package com.github.davishi25.statcheck.api.parser

import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.api.playerdata.StatsData
import com.github.davishi25.statcheck.api.playerdata.stats.SkyWarsData
import com.github.davishi25.statcheck.utils.Utils.roundToPrecision

object SkyWarsParser: ApiParser {
    data class SkyWarsContext(private val skyWarsData: SkyWarsData) {
        fun getWins() = skyWarsData.wins ?: 0
        fun getLosses() = skyWarsData.losses ?: 0
        fun getKills() = skyWarsData.kills ?: 0
        fun getDeaths() = skyWarsData.deaths ?: 0
        fun getKDR(): Double {
            val deaths = getDeaths()
            return getKills().toDouble() / (if(deaths == 0) 1 else deaths).toDouble()
        }
        fun getWLR(): Double {
            val losses = getLosses()
            return getWins().toDouble() / (if(losses == 0) 1 else losses).toDouble()
        }
        fun getStar(): Int {
            var exp = skyWarsData.skywars_experience ?: 0
            var level = 1

            val easyLevelsXp = arrayOf(
                10, 25, 50, 75, 100, 250, 500, 750, 1000,
                1250, 1500, 1750, 2000, 2500, 3000, 3500, 4000, 4500
            )

            easyLevelsXp.forEach { cost ->
                if (exp >= cost) {
                    exp -= cost
                    level++
                } else {
                    return level
                }
            }
            if (exp > 0) level += exp / 5000

            return level
        }
    }

    override fun getStatLine(statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = SkyWarsContext(statsData.skyWars)
        val star = ctx.getStar()
        val wins = ctx.getWins()
        val kills = ctx.getKills()
        val KDR = ctx.getKDR().roundToPrecision(2)
        val WLR = ctx.getWLR().roundToPrecision(2)
        return "§r§6Star: §r$star §8| §r§cK: §r$kills §8| §r§cKD: §r$KDR §8| §r§aW: §r$wins §8| §r§aWL: §r$WLR"
    }
    override fun getVitalStatLine(statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = SkyWarsContext(statsData.skyWars)
        val star = ctx.getStar()
        val kills = ctx.getKills()
        val KDR = ctx.getKDR().roundToPrecision(2)
        return "§r§6Star: §r$star §8| §r§cKills: §r$kills §8| §r§cKDR: §r$KDR"
    }

    override fun toString() = "SkyWars"
}
