package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class BridgeDuelsParser implements ApiParser{
    public static class BridgeDuelsContext {
        private final JsonObject bridgeDuelsObj;
        BridgeDuelsContext(JsonObject bridgeDuelsObj) { this.bridgeDuelsObj = bridgeDuelsObj; }

        public int getWins() {
            return getWins("duel") + getWins("doubles") + getWins("threes") +
                   getWins("four") + getWins("2v2v2v2") + getWins("3v3v3v3") + getCTFWins();
        }
        public int getWins(String mode) { return Util.safeGetInt(bridgeDuelsObj,"bridge_" + mode + "_wins"); }

        public int getLosses() {
            return getLosses("duel") + getLosses("doubles") + getLosses("threes") +
                    getLosses("four") + getLosses("2v2v2v2") + getLosses("3v3v3v3") + getCTFLosses();
        }
        public int getLosses(String mode) { return Util.safeGetInt(bridgeDuelsObj,"bridge_" + mode + "_losses"); }

        public int getCTFWins() { return Util.safeGetInt(bridgeDuelsObj,"capture_threes_wins"); }
        public int getCTFLosses() { return Util.safeGetInt(bridgeDuelsObj,"capture_threes_losses"); }

        public double getWLR() {
            int losses = getLosses();
            return (double)getWins() / (losses != 0 ? losses : 1);
        }


    }
    public String getStatLine(JsonObject playerObj) {
        BridgeDuelsContext ctx = new BridgeDuelsContext(playerObj.getAsJsonObject("Duels"));
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Util.roundToPrecision(ctx.getWLR(),2);
    }
    public String toString() {
        return "Bridge";
    }
}
