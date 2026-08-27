package com.github.davishi25.statcheck;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "statcheck", useMetadata=true)
public class StatCheck {
    static EntityPlayerSP user = Minecraft.getMinecraft().thePlayer;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CheckStatsCommand());
    }
}
