package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class BridgeDuelsParser implements ApiParser{
    public static class BridgeDuelsContext {
        private final JsonObject bridgeDuelsObj;
        BridgeDuelsContext(JsonObject bridgeDuelsObj) { this.bridgeDuelsObj = bridgeDuelsObj; }
        public int getWins() {
            return getSoloWins() + getDoublesWins() + getThreesWins() + getFoursWins() + getFourTeamsWins() + getCTFWins();
        }
        public int getLosses() {
            return getSoloLosses() + getDoublesLosses() + getThreesLosses() + getFoursLosses() + getFourTeamsLosses() + getCTFLosses();
        }
        public double getWLR() {
            int losses = getLosses();
            return (double)getWins() / (losses != 0 ? losses : 1);
        }

        public int getSoloWins() { return Util.safeGetInt(bridgeDuelsObj,"bridge_duel_wins"); }
        public int getDoublesWins() { return Util.safeGetInt(bridgeDuelsObj,"bridge_doubles_wins"); }
        public int getThreesWins() { return Util.safeGetInt(bridgeDuelsObj,"bridge_threes_wins"); }
        public int getFoursWins() { return Util.safeGetInt(bridgeDuelsObj,"bridge_four_wins"); }
        public int getFourTeamsWins() { return Util.safeGetInt(bridgeDuelsObj,"bridge_3v3v3v3_wins") + Util.safeGetInt(bridgeDuelsObj,"bridge_2v2v2v2_wins"); }
        public int getCTFWins() { return Util.safeGetInt(bridgeDuelsObj,"capture_threes_wins"); }
        public int getSoloLosses() { return Util.safeGetInt(bridgeDuelsObj,"bridge_duel_losses"); }
        public int getDoublesLosses() { return Util.safeGetInt(bridgeDuelsObj,"bridge_doubles_losses"); }
        public int getThreesLosses() { return Util.safeGetInt(bridgeDuelsObj,"bridge_threes_losses"); }
        public int getFoursLosses() { return Util.safeGetInt(bridgeDuelsObj,"bridge_four_losses"); }
        public int getFourTeamsLosses() { return Util.safeGetInt(bridgeDuelsObj,"bridge_3v3v3v3_losses") + Util.safeGetInt(bridgeDuelsObj,"bridge_2v2v2v2_losses"); }
        public int getCTFLosses() { return Util.safeGetInt(bridgeDuelsObj,"capture_threes_losses"); }
    }
    public String getStatLine(JsonObject playerObj) {
        BridgeDuelsContext ctx = new BridgeDuelsContext(playerObj.getAsJsonObject("Duels"));
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Util.roundToPrecision(ctx.getWLR(),2);
    }
    public String toString() {
        return "Bridge";
    }
}
