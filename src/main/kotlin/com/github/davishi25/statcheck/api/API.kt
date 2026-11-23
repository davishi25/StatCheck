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
    private val recentCalls = ConcurrentHashMap<String, CacheItem<PlayerData>>()
    private val potentialNicks = ConcurrentHashMap.newKeySet<String>()
    private const val EXPIRY_TIME = 180000L

    fun getAPI(playerName: String): PlayerData {
        val name = playerName.lowercase()
        getCachedData(name, true)?.let { return it }
        try {
            if (potentialNicks.contains(name)) throw Exception("§c$playerName is nicked!")

            val url = URL("https://api.hypixel.net/player?key=${Config.apiKey}&name=$name")
            val reader = InputStreamReader(url.openStream(), StandardCharsets.UTF_8)

            val apiResponse = gson.fromJson(reader, JsonObject::class.java)
            reader.close()
            if (apiResponse.get("player") is JsonNull) {
                potentialNicks.add(name)
                throw Exception("§c$playerName does not exist. Are they nicked?")
            }
            val playerResponse = apiResponse.getAsJsonObject("player")
            val playerData = gson.fromJson(playerResponse, PlayerData::class.java)
            recentCalls[name] = CacheItem(System.currentTimeMillis(), playerData)
            return playerData
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun getCachedData(playerName: String, shouldCheckExpire: Boolean = false): PlayerData? {
        val name = playerName.lowercase()
        val item = recentCalls[name] ?: return null

        if (!shouldCheckExpire || System.currentTimeMillis() - item.timestamp < EXPIRY_TIME) return item.data

        recentCalls.remove(name)
        return null
    }

    fun isNicked(name: String): Boolean = potentialNicks.contains(name.lowercase())

    fun clearCache() {
        recentCalls.clear()
        potentialNicks.clear()
    }

    data class CacheItem<T>(val timestamp: Long, val data: T)
}
