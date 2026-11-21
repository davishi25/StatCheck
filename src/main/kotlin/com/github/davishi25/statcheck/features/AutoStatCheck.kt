package com.github.davishi25.statcheck.features

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.event.impl.GameStartEvent
import com.github.davishi25.statcheck.event.impl.LocrawUpdateEvent
import com.github.davishi25.statcheck.utils.location.LocationUtils
import com.github.davishi25.statcheck.utils.StatUtils
import com.github.davishi25.statcheck.utils.TablistUtils
import com.github.davishi25.statcheck.utils.Utils
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.ConcurrentHashMap

object AutoStatCheck {
    private val obfuscatedGames = listOf("DUELS","BEDWARS")
    private val joinRegex = Regex("^(\\w+)\\s+has joined")
    private val checkedUsers: MutableSet<String> = ConcurrentHashMap.newKeySet()

    @SubscribeEvent
    fun onGameStart(event: GameStartEvent) {
        event.gametype ?: return
        if (event.gametype !in obfuscatedGames) return

        val names = TablistUtils.getNames(includeSelf = true)
        if (Config.debugMode) Utils.modMessage("Detected names: $names")

        names.forEach { name ->
            try {
                if(LocationUtils.gametype.equals("DUELS")) {
                    StatUtils.createDuelsStatMessage(name, LocationUtils.getDuelsGamemode())
                } else {
                    StatUtils.createMinimalStatMessage(name, LocationUtils.gametype!!.lowercase())
                }
            } catch (e: Exception) {
                Utils.modMessage("Encountered Error on name $name: ${e.message}")
            }
        }
    }

    @SubscribeEvent
    fun onLocrawUpdate(event: LocrawUpdateEvent) {
        if (!LocationUtils.isOnHypixel) return
        if (!LocationUtils.inGame) return
        if (LocationUtils.gametype in obfuscatedGames) return
        val names = TablistUtils.getNames(includeSelf = true)
        if (Config.debugMode) Utils.chat(names.toString())
        names.forEach { name ->
            if (name in checkedUsers) return@forEach
            checkedUsers.add(name)
            StatUtils.createMinimalStatMessage(name, LocationUtils.gametype!!.lowercase())
        }
    }

    @SubscribeEvent
    fun onChat(event: ClientChatReceivedEvent) {
        if (!LocationUtils.isOnHypixel) return
        if (!LocationUtils.inGame) return
        if (LocationUtils.gametype in obfuscatedGames) return
        val msg = event.message.unformattedText
        val match = joinRegex.find(msg)
        match ?: return
        val username = match.groups[1]?.value ?: return
        if (username in checkedUsers) return
        checkedUsers.add(username)
        StatUtils.createMinimalStatMessage(username, LocationUtils.gametype!!.lowercase())
    }

    @SubscribeEvent
    fun onWorldChange(event: WorldEvent.Unload) = checkedUsers.clear()
}