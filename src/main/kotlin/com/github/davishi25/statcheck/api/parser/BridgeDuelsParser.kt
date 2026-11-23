package com.github.davishi25.statcheck.api.parser

import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.roundToPrecision
import com.github.davishi25.statcheck.api.playerdata.StatsData
import com.github.davishi25.statcheck.api.playerdata.stats.DuelsData

object BridgeDuelsParser: ApiParser{
    class BridgeDuelsContext(bridgeDuelsData: DuelsData): DuelsParser.DuelsContext("bridge", bridgeDuelsData) {
        override fun getWins(): Int = super.getWins() + super.getWins("2v2v2v2") + super.getWins("3v3v3v3") + getCTFWins()
        override fun getLosses(): Int = super.getLosses() + super.getLosses("2v2v2v2") + super.getLosses("3v3v3v3") + getCTFLosses()

        fun getCTFWins() = getDuelsData().capture_threes_wins ?: 0
        fun getCTFLosses() = getDuelsData().capture_threes_losses ?: 0
    }
    override fun getStatLine(statsData: StatsData?): String {
        statsData?.duels ?: return Utils.MISSING_STATLINE_ERROR
        val ctx = BridgeDuelsContext(statsData.duels)
        return "§aW: §r${ctx.getWins()} §8| §cL: §r${ctx.getLosses()} §8| §bWLR: §r${ctx.getWLR().roundToPrecision(2)}"
    }
    override fun getVitalStatLine(statsData: StatsData?): String = getStatLine(statsData)

    override fun toString() = "Bridge Duels"
}
