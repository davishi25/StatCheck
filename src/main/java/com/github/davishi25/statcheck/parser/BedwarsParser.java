package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class BedwarsParser implements ApiParser{
    public String getStatLine(JsonObject playerObj) {
        return "§rW: §a" + getWins(playerObj) + "§r | Finals: " + getFinals(playerObj) + " | FKDR: " + Util.roundToPrecision(getFKDR(playerObj),2);
    }
    public int getWins(JsonObject playerObj) {
        return playerObj.getAsJsonObject("Bedwars").get("wins_bedwars").getAsInt();
    }
    public int getFinals(JsonObject playerObj) {
        return playerObj.getAsJsonObject("Bedwars").get("final_kills_bedwars").getAsInt();
    }
    public int getFinalDeaths(JsonObject playerObj) {
        return playerObj.getAsJsonObject("Bedwars").get("final_deaths_bedwars").getAsInt();
    }
    public double getFKDR(JsonObject playerObj) {
        return (double)getFinals(playerObj) / getFinalDeaths(playerObj);
    }
    public String toString() {
        return "BedWars";
    }
}
