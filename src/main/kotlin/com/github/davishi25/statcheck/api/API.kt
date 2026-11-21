package com.github.davishi25.statcheck.api

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.api.playerdata.PlayerData
import com.google.gson.Gson
import com.google.gson.JsonNull
import com.google.gson.JsonObject

import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.ConcurrentHashMap

object API {
    private val gson = Gson()
    private val recentCalls = ConcurrentHashMap<String, PlayerData>()
    private val potentialNicks = ConcurrentHashMap.newKeySet<String>()
    fun getAPI(playerName: String): PlayerData {
        val name = playerName.lowercase()
        if (recentCalls.containsKey(name)) return recentCalls[name]!!
        try {
            val url = URL("https://api.hypixel.net/player?key=${Config.apiKey}&name=$name")
            val reader = InputStreamReader(url.openStream(), StandardCharsets.UTF_8)

            val apiResponse = gson.fromJson(reader, JsonObject::class.java)
            if (apiResponse.get("player") is JsonNull) {
                potentialNicks.add(name)
                throw Exception("§c$playerName does not exist. Are they nicked?")
            }
            val playerResponse = apiResponse.getAsJsonObject("player")
            val playerData = gson.fromJson(playerResponse, PlayerData::class.java)
            recentCalls[name] = playerData
            return playerData
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getRecentCalls() = recentCalls
    fun getNicks(): ConcurrentHashMap.KeySetView<String, Boolean> = potentialNicks
}
