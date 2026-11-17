package com.github.davishi25.statcheck.parser

import com.github.davishi25.statcheck.Utils
import com.google.gson.JsonObject

object BridgeDuelsParser: ApiParser{
    class BridgeDuelsContext(bridgeDuelsObj: JsonObject): DuelsParser.DuelsContext("bridge", bridgeDuelsObj) {
        override fun getWins(): Int = super.getWins() + super.getWins("2v2v2v2") + super.getWins("3v3v3v3") + getCTFWins()
        override fun getLosses(): Int = super.getLosses() + super.getLosses("2v2v2v2") + super.getLosses("3v3v3v3") + getCTFLosses()

        fun getCTFWins() = getDuelsObj().get("capture_threes_wins")?.asInt ?: 0
        fun getCTFLosses() = getDuelsObj().get("capture_threes_losses")?.asInt ?: 0
    }
    override fun getStatLine(playerObj: JsonObject): String {
        val ctx = BridgeDuelsContext(playerObj.getAsJsonObject("Duels"))
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Utils.roundToPrecision(ctx.getWLR(),2)
    }
    override fun toString() = "Bridge Duels"
}
