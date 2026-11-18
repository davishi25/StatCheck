package com.github.davishi25.statcheck.utils

import com.github.davishi25.statcheck.utils.Utils.user
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object LocationUtils {
    var currentMode: String? = null
    var currentDuelsMode: String? = null
    private var awaitingLocraw = false
    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) {
        resetLocation()
    }
    @SubscribeEvent
    fun onEntityJoin(event: EntityJoinWorldEvent) {
        if (event.entity != user) return
        if (awaitingLocraw) return
        awaitingLocraw = true
        Utils.sendMessage("/locraw")
    }
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!awaitingLocraw) return
        val message = event.message.unformattedText
        if (!message.startsWith('{')) return
        val locraw = JsonParser().parse(message).asJsonObject
        currentMode = locraw["gametype"]?.asString
        if(currentMode == "DUELS")
            currentDuelsMode = getDuelsGamemode(locraw)
        awaitingLocraw = false
        //Utils.modMessage("Detected mode: $currentMode duels mode: $currentDuelsMode")
        event.isCanceled = true
    }

    fun getDuelsGamemode(obj: JsonObject): String? {
        if(obj.get("mode") == null) return null
        val gamemode = obj.get("mode").asString.lowercase()
        //Utils.modMessage("gamemode from locraw: $gamemode")
        Utils.duelsModes.forEach { mode ->
            if(gamemode.contains(mode)) return mode
        }
        return null
    }

    fun resetLocation() {
        currentMode = null
        currentDuelsMode = null
        awaitingLocraw = false
    }
}