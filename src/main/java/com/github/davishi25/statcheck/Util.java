package com.github.davishi25.statcheck;

import com.github.davishi25.statcheck.parser.ApiParser;
import com.github.davishi25.statcheck.parser.BedwarsParser;
import com.github.davishi25.statcheck.parser.BridgeDuelsParser;
import com.github.davishi25.statcheck.parser.DuelsParser;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static final Map<String,String> ranks = new HashMap();
    public static final Map<String,String> rankColors = new HashMap();
    public static final Map<String, ApiParser> parsers = new HashMap();

    static {
        ranks.put("VIP","§a[VIP]");
        ranks.put("VIP_PLUS","§a[VIP§6+§a]");
        ranks.put("MVP","§b[MVP]");

        rankColors.put(null,"§c");
        rankColors.put("RED","§c");
        rankColors.put("ORANGE","§6");
        rankColors.put("GREEN","§a");
        rankColors.put("YELLOW","§e");
        rankColors.put("LIGHT_PURPLE","§d");
        rankColors.put("WHITE","§f");
        rankColors.put("BLUE","§9");
        rankColors.put("DARK_GREEN","§2");
        rankColors.put("DARK_RED","§4");
        rankColors.put("DARK_AQUA","§3");
        rankColors.put("DARK_PURPLE","§5");
        rankColors.put("DARK_GRAY","§8");
        rankColors.put("BLACK","§0");
        rankColors.put("DARK_BLUE","§1");

        final ApiParser bwParser = new BedwarsParser();
        parsers.put("bw", bwParser);
        parsers.put("bedwars", bwParser);
        final ApiParser duelsParser = new DuelsParser();
        parsers.put("d",duelsParser);
        parsers.put("duels",duelsParser);
        final ApiParser bridgeDuelsParser = new BridgeDuelsParser();
        parsers.put("b",bridgeDuelsParser);
        parsers.put("bridge",bridgeDuelsParser);
    }

    public static String getFormattedName(String name) { return getFormattedName(API.getAPI(name)); }

    public static String getFormattedName(JsonObject playerObj) {
        String username = playerObj.get("displayname").getAsString();
        String prefix = "§7";
        String rank = playerObj.get("newPackageRank") != null ? playerObj.get("newPackageRank").getAsString() : null;
        String rankColor = playerObj.get("rankPlusColor") != null ? playerObj.get("rankPlusColor").getAsString() : null;
        boolean isMVPPlusPlus = !(playerObj.get("monthlyPackageRank") == null || playerObj.get("monthlyPackageRank").getAsString().equals("NONE"));

        if (isMVPPlusPlus) {
            final boolean aquaTag = playerObj.get("monthlyRankColor") != null && playerObj.get("monthlyRankColor").getAsString().equals("AQUA");
            prefix = aquaTag ? "§b[MVP" + rankColors.get(rankColor) + "++§b]" : "§6[MVP" + rankColors.get(rankColor) + "++§6]";
        } else if (rank != null) {
            if (rank.equals("MVP_PLUS")) {
                prefix = "§b[MVP" + rankColors.get(rankColor) + "+§b]";
            } else if (ranks.containsKey(rank)){
                prefix = ranks.get(rank);
            }
        }

        return prefix + " " + username + "§r";
    }

    public static String getNameLine(JsonObject playerObj, ApiParser game) {
        return getFormattedName(playerObj) + "'s §r" + game.toString() + "\n";
    }

    public static double roundToPrecision(double n, int decimals) {
        long scale = (long)Math.pow(10, decimals);
        return (double)((int)(n * scale)) / scale;
    }

    public static String stripColorCodes(String s) { return s.replaceAll("§.", ""); }

    public static int safeGetInt(JsonObject obj, String target) {
        return obj.has(target) ? obj.get(target).getAsInt() : 0;
    }
    public static double safeGetDouble(JsonObject obj, String target) {
        return obj.has(target) ? obj.get(target).getAsDouble() : 0.0;
    }
}
