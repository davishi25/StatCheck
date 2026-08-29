package com.github.davishi25.statcheck;


import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class API {
    private static final Gson gson = new Gson();
    private static final Map<String,JsonObject> recentCalls = new HashMap();

    //returns the player section of the Hypixel API call
    static JsonObject getAPI(String playerName) {
        if(recentCalls.containsKey(playerName)) return recentCalls.get(playerName);
        try {
            URL url = new URL("https://api.hypixel.net/player?key=" + Config.apiKey + "&name=" + playerName);
            Reader reader = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);

            JsonObject apiResponse = gson.fromJson(reader, JsonObject.class);
            if(apiResponse.get("player") instanceof JsonNull) throw new Exception("§c" + playerName + " does not exist. Are they nicked?");
            JsonObject playerResponse = apiResponse.getAsJsonObject("player");
            recentCalls.put(playerName,playerResponse);
            return playerResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
