package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class DuelsParser implements ApiParser{
    public static class DuelsContext {
        private final JsonObject duelsObj;
        DuelsContext(JsonObject duelsObj) { this.duelsObj = duelsObj; }

        public int getWins() { return Util.safeGetInt(duelsObj,"wins"); }
        public int getLosses() { return Util.safeGetInt(duelsObj,"losses"); }
        public double getWLR() {
            int losses = getLosses();
            return (double)getWins() / (losses != 0 ? losses : 1);
        }
    }
    public String getStatLine(JsonObject playerObj) {
        DuelsContext ctx = new DuelsContext(playerObj.getAsJsonObject("Duels"));
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Util.roundToPrecision(ctx.getWLR(),2);
    }
    public String toString() {
        return "Duels";
    }
}
