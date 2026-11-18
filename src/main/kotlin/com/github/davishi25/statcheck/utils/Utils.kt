package com.github.davishi25.statcheck.utils

import com.github.davishi25.statcheck.API
import com.github.davishi25.statcheck.parser.ApiParser
import com.github.davishi25.statcheck.playerdata.PlayerData
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityPlayerSP
import net.minecraft.util.ChatComponentText
import kotlin.math.pow
import kotlin.reflect.full.memberProperties

object Utils {
    val mc: Minecraft get() = Minecraft.getMinecraft()
    val user: EntityPlayerSP get() = Minecraft.getMinecraft().thePlayer

    const val MISSING_STATLINE_ERROR = "§cCould not fetch stats"

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
    val duelsModes = listOf(
        "bridge",
        "sumo",
        "uhc",
        "sw",
        "mw",
        "bow",
        "bowspleef",
        "spleef",
        "boxing",
        "combo",
        "blitz",
        "op",
        "parkour",
        "quake",
        "potion",
        "classic"
    )

    fun chat(msg: String?) = user.addChatMessage(ChatComponentText(msg))
    fun modMessage(msg: String?) = chat("§6[SC] §r$msg")

    fun sendMessage(msg: String) = user.sendChatMessage(msg)

    fun getFormattedName(name: String): String = getFormattedName(API.getAPI(name))

    fun getFormattedName(playerData: PlayerData): String {
        val username = playerData.displayname
        val rank = playerData.newPackageRank
        val rankColor = playerData.rankPlusColor
        val isMVPPlusPlus = playerData.monthlyPackageRank != null && playerData.monthlyPackageRank != "NONE"
        val prefix = when {
            isMVPPlusPlus -> {
                val aquaTag = playerData.monthlyRankColor == "AQUA"
                val colorCode = if (aquaTag) "§b" else "§6"
                "$colorCode[MVP${rankColors[rankColor]}++$colorCode]"
            }

            rank == "MVP_PLUS" -> "§b[MVP${rankColors[rankColor]}+§b]"
            rank != null && ranks.containsKey(rank) -> ranks[rank]!!
            else -> "§7"
        }

        return "$prefix $username§r"
    }

    fun getNameLine(playerData: PlayerData, game: ApiParser): String = "${getFormattedName(playerData)}'s §r${game}\n"

    fun Double.roundToPrecision(decimals: Int): Double {
        val scale = 10.0.pow(decimals)
        return (((this * scale).toInt()).toDouble()) / scale
    }

    fun String.stripColorCodes(): String = replace("§.", "")

    fun Any.getPropertyByName(variableName: String): Any? {
        val property = this::class.memberProperties.find { it.name == variableName }
        return property?.call(this)
    }
}
