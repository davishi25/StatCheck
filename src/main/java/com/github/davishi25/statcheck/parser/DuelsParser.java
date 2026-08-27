package com.github.davishi25.statcheck.parser;

import com.github.davishi25.statcheck.Util;
import com.google.gson.JsonObject;

public class DuelsParser implements ApiParser{
    public String getStatLine(JsonObject playerObj) {
        return "§rW: §a" + getWins(playerObj) + "§r | WLR: " + Util.roundToPrecision(getWLR(playerObj),2);
    }
    public int getWins(JsonObject playerObj) {
        return playerObj.getAsJsonObject("Duels").get("wins").getAsInt();
    }
    public int getLosses(JsonObject playerObj) {
        return playerObj.getAsJsonObject("Duels").get("losses").getAsInt();
    }
    public double getWLR(JsonObject playerObj) {
        return (double)getWins(playerObj) / getLosses(playerObj);
    }
    public String toString() {
        return "Duels";
    }
}
