package com.github.davishi25.statcheck;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class API {
    private static final Gson gson = new Gson();
    //temp api key, will remove in the future
    private static final String key = "34403bdd-121f-4efb-8973-180786d5b224";
    private static final Map<String,String> gamemodes = new HashMap();
    private static final Map<String,JsonObject> recentCalls = new HashMap();

    API() {
        gamemodes.put("bw","Bedwars");
    }
    static JsonObject getAPI(String playerName) {
        if(recentCalls.containsKey(playerName)) return recentCalls.get(playerName);

        try {
            URL url = new URL("https://api.hypixel.net/player?key=" + key + "&name=" + playerName);
            Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
            JsonObject apiResponse = gson.fromJson(reader, JsonObject.class).getAsJsonObject("player");
            recentCalls.put(playerName,apiResponse);
            return apiResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int lookupWins(String player, String game) {
        JsonObject apiResponse = getAPI(player).getAsJsonObject("stats");
        JsonElement winCount = apiResponse.getAsJsonObject(gamemodes.getOrDefault(game,"Bedwars")).get("wins_bedwars");
        return winCount != null ? winCount.getAsInt() : 0;
    }
}
