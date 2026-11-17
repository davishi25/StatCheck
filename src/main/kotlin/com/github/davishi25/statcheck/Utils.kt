package com.github.davishi25.statcheck

import com.github.davishi25.statcheck.parser.ApiParser
import com.google.gson.JsonObject
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.ChatComponentText
import kotlin.math.pow

object Utils {
    val mc: Minecraft get() = Minecraft.getMinecraft()
    val user: EntityPlayerSP get() = Minecraft.getMinecraft().thePlayer

    val ranks = mapOf(
        "VIP" to "§a[VIP]",
        "VIP_PLUS" to "§a[VIP§6+§a]",
        "MVP" to "§b[MVP]"
    )
    val rankColors = mapOf(
        null to "§c",
        "RED" to "§c",
        "GOLD" to "§6",
        "GREEN" to "§a",
        "YELLOW" to "§e",
        "LIGHT_PURPLE" to "§d",
        "WHITE" to "§f",
        "BLUE" to "§9",
        "DARK_GREEN" to "§2",
        "DARK_RED" to "§4",
        "DARK_AQUA" to "§3",
        "DARK_PURPLE" to "§5",
        "DARK_GRAY" to "§8",
        "BLACK" to "§0",
        "DARK_BLUE" to "§1"
    )
    val duelsModes = listOf("bridge","sumo","uhc","sw","mw","bow","bowspleef","spleef","boxing","combo","blitz","op","parkour","quake","potion")

    fun chat(msg: String?) = user.addChatMessage(ChatComponentText(msg))
    fun modMessage(msg: String?) = chat("§6[SC] §r$msg")

    fun getFormattedName(name: String): String = getFormattedName(API.getAPI(name))

    fun getFormattedName(playerObj: JsonObject): String {
        val username = playerObj.get("displayname").asString
        val rank = playerObj.get("newPackageRank")?.asString
        val rankColor = playerObj.get("rankPlusColor")?.asString
        val isMVPPlusPlus = playerObj.has("monthlyPackageRank") && playerObj.get("monthlyPackageRank")?.asString != "NONE"
        val prefix = when {
            isMVPPlusPlus -> {
                val aquaTag = playerObj.get("monthlyRankColor")?.asString.equals("AQUA")
                val colorCode = if(aquaTag) "§b" else "§6"
                "$colorCode[MVP${rankColors[rankColor]}++$colorCode]"
            }
            rank == "MVP_PLUS" -> "§b[MVP${rankColors[rankColor]}+§b]"
            rank != null && ranks.containsKey(rank) -> ranks[rank]!!
            else -> "§7"
        }

        return "$prefix $username§r"
    }

    fun getNameLine(playerObj: JsonObject, game: ApiParser): String = "${getFormattedName(playerObj)}'s §r${game}\n"

    fun roundToPrecision(n : Double, decimals: Int): Double {
        val scale = 10.0.pow(decimals)
        return (((n * scale).toInt()).toDouble()) / scale
    }

    fun String.stripColorCodes(): String = replace("§.", "")
}
