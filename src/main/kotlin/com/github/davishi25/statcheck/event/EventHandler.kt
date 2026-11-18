package com.github.davishi25.statcheck.event

import com.github.davishi25.statcheck.utils.StatUtils
import com.github.davishi25.statcheck.utils.Utils
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import net.minecraft.client.Minecraft
import com.github.davishi25.statcheck.utils.Utils.user

import net.minecraft.util.ChatComponentText
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

class EventHandler {
    var newWorld = false
    var isGameStarting = false
    var mostRecentGame: String? = null
    var mostRecentDuelsGamemode: String? = null
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
        }
    }
    // TODO: ignore npcs
    @SubscribeEvent
    fun statTick(event: TickEvent.ClientTickEvent) {
        if(event.phase != TickEvent.Phase.START) return
        if(statTick > 0) {
            statTick--
        } else if(statTick == 0) {
            val tablist = Minecraft.getMinecraft().netHandler.playerInfoMap
            val names = mutableSetOf<String>()
            tablist.forEach { playerInfo ->
                val name = playerInfo.gameProfile.name
                if (name == user.name) return@forEach
                names.add(name)
            }
            
            names.forEach { name ->
                try {
                    if(mostRecentGame.equals("duels")) {
                        StatUtils.createDuelsStatMessage(name, mostRecentDuelsGamemode!!)
                    } else {
                        StatUtils.createStatMessage(name, mostRecentGame!!)
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

    private var awaitingLocation = false
    private var hasPolledWorld = false
    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Load) {
        hasPolledWorld = false
        awaitingLocation = false
    }
    private var pollTick = -1
    @SubscribeEvent //when you join the world, start a one-second timer
    fun onEntityJoin(event: EntityJoinWorldEvent) {
        if(event.entity != user) return
        pollTick = 20
    }
    @SubscribeEvent //tick down the timer and send /locraw after one second
    fun onTick(event: TickEvent.ClientTickEvent) {
        if(event.phase != TickEvent.Phase.START) return
        if(hasPolledWorld) return
        if(pollTick > 0) {
            pollTick--
        } else if(pollTick == 0) {
            pollTick = -1

            user.sendChatMessage("/locraw")
            awaitingLocation = true
            hasPolledWorld = true
        }
    }
    @SubscribeEvent
    fun determineLocation(event: ClientChatReceivedEvent) {
        if(!awaitingLocation) return
        if(event.message == null) return
        val jsonString = event.message.unformattedText
        //sketchy solution may not work
        if(!jsonString.startsWith("{")) return

        val jsonObject = JsonParser().parse(jsonString).getAsJsonObject()
        val isInGame = jsonObject.get("gametype") != null
        mostRecentGame = if(isInGame) jsonObject.get("gametype").asString.lowercase() else "lobby"
        //player.addChatMessage(new ChatComponentText("Detected game: " + mostRecentGame))

        //try to detect a duels gamemode
        val duelsGameMode = getDuelsGamemode(jsonObject)
        if(mostRecentGame.equals("duels") && duelsGameMode != null)
            mostRecentDuelsGamemode = duelsGameMode
        awaitingLocation = false
        event.setCanceled(true)
    }
    fun getDuelsGamemode(obj: JsonObject): String? {
        if(obj.get("mode") == null) return null
        val gamemode = obj.get("mode").asString.uppercase()
        Utils.duelsModes.forEach { mode ->
            if(gamemode.contains(mode)) return mode
        }
        return null
    }
}
