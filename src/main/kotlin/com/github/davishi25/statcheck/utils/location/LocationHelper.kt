package com.github.davishi25.statcheck.utils.location

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.event.impl.LocrawUpdateEvent
import com.github.davishi25.statcheck.event.impl.UserJoinEvent
import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.location.LocationUtils.gametype
import com.github.davishi25.statcheck.utils.location.LocationUtils.inGame
import com.github.davishi25.statcheck.utils.location.LocationUtils.isOnHypixel
import com.github.davishi25.statcheck.utils.location.LocationUtils.mode
import com.google.gson.JsonParser
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object LocationHelper {
    private var awaitingLocraw = false
    private var waitTicks = 0
    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) = resetLocation()

    @SubscribeEvent
    fun onUserJoin(event: UserJoinEvent) {
        if (!isOnHypixel) return
        if (awaitingLocraw) return
        awaitingLocraw = true
        waitTicks = 5
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!awaitingLocraw) return
        val message = event.message.unformattedText
        if (!message.startsWith('{')) return
        val locraw = JsonParser().parse(message).asJsonObject
        gametype = locraw["gametype"]?.asString
        mode = locraw["mode"]?.asString
        awaitingLocraw = false
        if (Config.debugMode) {
            Utils.modMessage("raw locraw: $message")
            Utils.modMessage("Detected mode: $gametype, duels mode: $mode, in game: $inGame")
        }
        waitTicks = 0
        MinecraftForge.EVENT_BUS.post(LocrawUpdateEvent())
        event.isCanceled = true
    }
    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (!awaitingLocraw) return
        if (waitTicks <= 0) return
        if (--waitTicks == 0) Utils.sendMessage("/locraw")
    }
    fun resetLocation() {
        gametype = null
        mode = null
        awaitingLocraw = false
        waitTicks = 0
    }
}