package com.github.davishi25.statcheck.event

import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object EventHandler {
    private var newWorld = false
    private var isGameStarting = false
    private var statTick = -1

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
            MinecraftForge.EVENT_BUS.post(GameStartEvent())
            statTick = -1
        }
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) {
        newWorld = true
        isGameStarting = false
    }
}
