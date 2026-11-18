package com.github.davishi25.statcheck.api.parser

import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.getPropertyByName
import com.github.davishi25.statcheck.utils.Utils.roundToPrecision
import com.github.davishi25.statcheck.api.playerdata.StatsData
import com.github.davishi25.statcheck.api.playerdata.stats.DuelsData

object DuelsParser : ApiParser {
    open class DuelsContext(private val gamemode: String?, private val duelsData: DuelsData) {
        fun getDuelsData() = duelsData

        open fun getWins(): Int {
            gamemode ?: return duelsData.wins ?: 0
            return getWins("duel") + getWins("doubles") + getWins("threes") + getWins("four")
        }

        fun getWins(mode: String): Int = (duelsData.getPropertyByName("${gamemode}_${mode}_wins") as Int?) ?: 0

        open fun getLosses(): Int {
            gamemode ?: return duelsData.losses ?: 0
            return getLosses("duel") + getLosses("doubles") + getLosses("threes") + getLosses("four")
        }

        fun getLosses(mode: String) = (duelsData.getPropertyByName("${gamemode}_${mode}_losses") as Int?) ?: 0

        fun getWLR(): Double {
            val losses = getLosses()
            return getWins().toDouble() / (if (losses == 0) 1 else losses).toDouble()
        }
    }

    override fun getStatLine(statsData: StatsData?) = getStatLine(null, statsData)
    fun getStatLine(gamemode: String?, statsData: StatsData?): String {
        statsData ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = DuelsContext(gamemode, statsData.duels)
        return "§rW: §a${ctx.getWins()}§r | L: §c${ctx.getLosses()}§r | WLR: ${ctx.getWLR().roundToPrecision(2)}"
    }
    override fun getVitalStatLine(statsData: StatsData?): String = BedwarsParser.getStatLine(statsData)

    override fun toString() = "Duels"
}
