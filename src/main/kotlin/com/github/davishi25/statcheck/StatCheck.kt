package com.github.davishi25.statcheck

import com.github.davishi25.statcheck.commands.ApiSetCommand
import com.github.davishi25.statcheck.commands.CheckStatsCommand
import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.event.EventHandler
import com.github.davishi25.statcheck.features.AutoStatCheck
import com.github.davishi25.statcheck.utils.LocationUtils
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

@Mod(modid = "statcheck", useMetadata=true)
class StatCheck {
    @Mod.EventHandler
    fun init(event: FMLInitializationEvent ) {
        MinecraftForge.EVENT_BUS.register(EventHandler)
        MinecraftForge.EVENT_BUS.register(LocationUtils)
        MinecraftForge.EVENT_BUS.register(AutoStatCheck)
        MinecraftForge.EVENT_BUS.register(this)
        ClientCommandHandler.instance.registerCommand(CheckStatsCommand())
        ClientCommandHandler.instance.registerCommand(ApiSetCommand())
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        Config.init(event.suggestedConfigurationFile)
    }
}
