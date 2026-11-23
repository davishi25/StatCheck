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
        fun getStar(): String = skyWarsData.levelFormattedWithBrackets ?: "[1*]"
    }

    override fun getStatLine(statsData: StatsData?): String {
        statsData?.skyWars ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = SkyWarsContext(statsData.skyWars)
        val star = ctx.getStar()
        val wins = ctx.getWins()
        val kills = ctx.getKills()
        val KDR = ctx.getKDR().roundToPrecision(2)
        val WLR = ctx.getWLR().roundToPrecision(2)
        return "§r$star§8| §r§cK: §r$kills §8| §r§cKD: §r$KDR §8| §r§aW: §r$wins §8| §r§aWL: §r$WLR"
    }
    override fun getVitalStatLine(statsData: StatsData?): String {
        statsData?.skyWars ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = SkyWarsContext(statsData.skyWars)
        val star = ctx.getStar()
        val kills = ctx.getKills()
        val KDR = ctx.getKDR().roundToPrecision(2)
        return "§r$star§8| §r§cKills: §r$kills §8| §r§cKDR: §r$KDR"
    }

    override fun toString() = "SkyWars"
}
