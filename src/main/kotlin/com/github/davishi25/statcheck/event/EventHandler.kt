package com.github.davishi25.statcheck.event

import com.github.davishi25.statcheck.utils.LocationUtils
import com.github.davishi25.statcheck.utils.StatUtils
import com.github.davishi25.statcheck.utils.Utils.mc
import com.github.davishi25.statcheck.utils.Utils.user

import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object EventHandler {
    var newWorld = false
    var isGameStarting = false
    var statTick = -1
    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if(!newWorld) return
        if(event.message.unformattedText.equals("The game starts in 1 second!")) {
            //user.addChatMessage(new ChatComponentText("Detected game starting soon!"))
            isGameStarting = true
            return
        }
        if(isGameStarting && event.message.unformattedText.contains("▬")) {
            //user.addChatMessage(new ChatComponentText("Detected that you started " + mostRecentGame + "!"))
            statTick = 30
            newWorld = false
            isGameStarting = false
        }
    }
    // TODO: ignore npcs
    @SubscribeEvent
    fun statTick(event: TickEvent.ClientTickEvent) {
        if(event.phase != TickEvent.Phase.START) return
        if(statTick > 0) {
            statTick--
        } else if(statTick == 0) {
            val tablist = mc.netHandler.playerInfoMap
            val names = mutableSetOf<String>()
            tablist.forEach { playerInfo ->
                val name = playerInfo.gameProfile.name
                if (name == user.name) return@forEach
                names.add(name)
            }
            
            names.forEach { name ->
                try {
                    if(LocationUtils.currentMode.equals("duels")) {
                        StatUtils.createDuelsStatMessage(name, LocationUtils.currentDuelsMode!!)
                    } else {
                        StatUtils.createStatMessage(name, LocationUtils.currentMode!!)
                    }
                } catch (e: Exception) {
                    user.addChatMessage(ChatComponentText("Encountered Error on name $name: ${e.message}"))
                }
            }
            statTick = -1
        }
    }
    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) {
        newWorld = true
        isGameStarting = false
    }
}
