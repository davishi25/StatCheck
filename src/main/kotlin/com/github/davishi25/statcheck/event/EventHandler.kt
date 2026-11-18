package com.github.davishi25.statcheck.event

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.utils.LocationUtils
import com.github.davishi25.statcheck.utils.Utils
import com.github.davishi25.statcheck.utils.Utils.user
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object EventHandler {
    private var newWorld = false
    private var isGameStarting = false
    private var statTick = -1
    private var joinTick = -1

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!newWorld) return
        val msg = event.message.unformattedText

        if (isGameStarting && msg.contains("▬")) {
            statTick = 30
            newWorld = false
            isGameStarting = false
        }
        if (msg == "The game starts in 1 second!") isGameStarting = true
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START) return
        if (statTick > 0) {
            statTick --
        } else if (statTick == 0) {
            MinecraftForge.EVENT_BUS.post(GameStartEvent(LocationUtils.gametype, LocationUtils.mode))
            if (Config.debugMode) Utils.modMessage("Detected Game Start!")
            statTick = -1
        }
        if (joinTick > 0) {
            joinTick --
        } else if (joinTick == 0) {
            MinecraftForge.EVENT_BUS.post(UserJoinEvent())
            if (Config.debugMode) Utils.modMessage("Detected User Join!")
            joinTick = -1
        }
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) {
        newWorld = true
        isGameStarting = false
    }

    @SubscribeEvent
    fun onEntityJoin(event: EntityJoinWorldEvent) {
        if (event.entity != user) return
        joinTick = 10 // assume player loads in within 10t
    }
}
