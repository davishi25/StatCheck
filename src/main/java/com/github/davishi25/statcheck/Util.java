package com.github.davishi25.statcheck;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Util {
    static final Map<String,String> ranks = new HashMap();
    static final Map<String,String> rankColors = new HashMap();
    Util() {
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
    }

    static String formattedName(String name) {
        JsonObject apiObject = API.getAPI(name);
        String username = apiObject.get("displayname").getAsString();
        String prefix = "§7";
        String rank = apiObject.get("newPackageRank") != null ? apiObject.get("newPackageRank").getAsString() : null;
        String rankColor = apiObject.get("rankPlusColor") != null ? apiObject.get("rankPlusColor").getAsString() : null;
        boolean isMVPPlusPlus = !(apiObject.get("monthlyPackageRank") == null || apiObject.get("monthlyPackageRank").getAsString().equals("NONE"));

        if (isMVPPlusPlus) {
            final boolean aquaTag = apiObject.get("monthlyRankColor") != null && apiObject.get("monthlyRankColor").getAsString().equals("AQUA");
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
}
