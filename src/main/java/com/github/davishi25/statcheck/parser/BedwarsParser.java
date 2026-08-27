package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class BedwarsParser implements ApiParser {
    public static class BedwarsContext {
        private final JsonObject bedwarsObj;

        BedwarsContext(JsonObject bedwarsObj) {
            this.bedwarsObj = bedwarsObj;
        }
        public int getWins() { return Util.safeGetInt(bedwarsObj,"wins_bedwars"); }
        public int getFinals() { return Util.safeGetInt(bedwarsObj,"final_kills_bedwars"); }
        public int getFinalDeaths() { return Util.safeGetInt(bedwarsObj,"final_deaths_bedwars"); }
        public double getFKDR() {
            int deaths = getFinalDeaths();
            return (double)getFinals() / (deaths != 0 ? deaths : 1);
        }
    }
    public String getStatLine(JsonObject playerObj) {
        BedwarsContext ctx = new BedwarsContext(playerObj.getAsJsonObject("Bedwars"));
        return "§rW: §a" + ctx.getWins() + "§r | Finals: " + ctx.getFinals() + " | FKDR: " + Util.roundToPrecision(ctx.getFKDR(),2);
    }

    public String toString() {
        return "BedWars";
    }
}
