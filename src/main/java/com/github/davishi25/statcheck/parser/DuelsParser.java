package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class DuelsParser implements ApiParser{
    public static class DuelsContext {
        private final JsonObject duelsObj;
        private final String gamemode;
        DuelsContext(String gamemode, JsonObject duelsObj) {
            this.duelsObj = duelsObj;
            this.gamemode = gamemode;
        }

        public JsonObject getDuelsObj() { return duelsObj; }

        public int getWins() {
            if(gamemode.isEmpty()) return Util.safeGetInt(duelsObj,"wins");
            return getWins("duel") + getWins("doubles") + getWins("threes") +
                    getWins("four");
        }
        public int getWins(String mode) { return Util.safeGetInt(duelsObj,gamemode + "_" + mode + "_wins"); }

        public int getLosses() {
            if(gamemode.isEmpty()) return Util.safeGetInt(duelsObj,"losses");
            return getLosses("duel") + getLosses("doubles") + getLosses("threes") +
                    getLosses("four");
        }
        public int getLosses(String mode) { return Util.safeGetInt(duelsObj,gamemode + "_" + mode + "_losses"); }

        public double getWLR() {
            int losses = getLosses();
            return (double)getWins() / (losses != 0 ? losses : 1);
        }
    }
    public String getStatLine(JsonObject playerObj) { return getStatLine("",playerObj); }
    public String getStatLine(String gamemode, JsonObject playerObj) {
        if(gamemode == null) gamemode = "";
        DuelsContext ctx = new DuelsContext(gamemode, playerObj.getAsJsonObject("Duels"));
        return "§rW: §a" + ctx.getWins() + "§r | L: §c" + ctx.getLosses() + "§r | WLR: " + Util.roundToPrecision(ctx.getWLR(),2);
    }
    public String toString() {
        return "Duels";
    }
}
