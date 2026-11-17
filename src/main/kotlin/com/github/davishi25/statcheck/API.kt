package com.github.davishi25.statcheck


import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonObject

import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets

object API {
    private val gson = Gson()
    private val recentCalls = mutableMapOf<String,JsonObject>()

    //returns the player section of the Hypixel API call
     fun getAPI(playerName: String): JsonObject {
        if(recentCalls.containsKey(playerName)) return recentCalls[playerName]!!
        try {
            val url = URL("https://api.hypixel.net/player?key=${Config.apiKey}&name=$playerName")
            val reader = InputStreamReader(url.openStream(), StandardCharsets.UTF_8)

            val apiResponse = gson.fromJson(reader, JsonObject::class.java)
            if(apiResponse.get("player") is JsonNull) throw Exception("§c$playerName does not exist. Are they nicked?")
            val playerResponse = apiResponse.getAsJsonObject("player")
            recentCalls[playerName] = playerResponse
            return playerResponse
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getRecentCalls() = recentCalls
}
