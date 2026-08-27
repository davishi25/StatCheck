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
    static API api = new API();
    static Util util = new Util();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        MinecraftForge.EVENT_BUS.register(this);
        ClientCommandHandler.instance.registerCommand(new CheckStatsCommand());
    }
    static void checkStats(String player, String game) {
        Thread thread = new Thread(() -> {
            try {
                int wins = API.lookupWins(player, game);

                user.addChatMessage(new ChatComponentText(Util.formattedName(player) + " | " + game + " wins: " + wins));
            } catch (Exception e) {
                user.addChatMessage(new ChatComponentText("§cEncountered an error, is your API key wrong?"));
                System.out.println(e);
            }
        });
        thread.start();
    }

}
