package com.github.davishi25.statcheck;


import com.google.gson.Gson;
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
    private static final String key = "457a2b76-6a94-4372-ad0d-69080b0eec99";
    private static final Map<String,JsonObject> recentCalls = new HashMap();

    //returns the player section of the Hypixel API call
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
}
