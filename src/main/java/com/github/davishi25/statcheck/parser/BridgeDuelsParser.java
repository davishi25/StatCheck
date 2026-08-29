package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class BridgeDuelsParser implements ApiParser{
    public static class BridgeDuelsContext extends DuelsParser.DuelsContext {
        BridgeDuelsContext(JsonObject bridgeDuelsObj) {
            super("bridge",bridgeDuelsObj);
        }

        public int getWins() {
            return super.getWins() + super.getWins("2v2v2v2") + super.getWins("3v3v3v3") + getCTFWins();
        }

        public int getLosses() {
            return super.getLosses() + super.getLosses("2v2v2v2") + super.getLosses("3v3v3v3") + getCTFLosses();
        }

        public int getCTFWins() { return Util.safeGetInt(getDuelsObj(),"capture_threes_wins"); }
        public int getCTFLosses() { return Util.safeGetInt(getDuelsObj(),"capture_threes_losses"); }
    }
    public String getStatLine(JsonObject playerObj) {
        BridgeDuelsContext ctx = new BridgeDuelsContext(playerObj.getAsJsonObject("Duels"));
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Util.roundToPrecision(ctx.getWLR(),2);
    }
    public String toString() {
        return "Bridge Duels";
    }
}
