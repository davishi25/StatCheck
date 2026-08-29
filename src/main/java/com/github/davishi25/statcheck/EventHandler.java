package com.github.davishi25.statcheck;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventHandler {
    private boolean newWorld = false;
    private boolean isGameStarting = false;
    private String mostRecentGame;
    private String mostRecentDuelsGamemode;
    private int statTick = -1;
    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(!newWorld) return;
        if(event.message.getUnformattedText().equals("The game starts in 1 second!")) {
            //user.addChatMessage(new ChatComponentText("Detected game starting soon!"));
            isGameStarting = true;
            return;
        }
        if(isGameStarting && event.message.getUnformattedText().contains("▬")) {
            //user.addChatMessage(new ChatComponentText("Detected that you started " + mostRecentGame + "!"));
            statTick = 30;
            newWorld = false;
        }
    }

    @SubscribeEvent
    public void statTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;
        if(statTick > 0) {
            statTick--;
        } else if(statTick == 0) {
            final EntityPlayerSP user = Minecraft.getMinecraft().thePlayer;
            final Collection<NetworkPlayerInfo> temp = Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap();
            final Set<String> names = new HashSet();
            for(NetworkPlayerInfo i : temp) {
                String name = i.getGameProfile().getName();
                //user.addChatMessage(new ChatComponentText("detected player: " + name));
                if(name.equals(user.getName())) continue;
                names.add(name);
            }
            for(String name : names) {
                try {
                    if(mostRecentGame.equals("duels")) {
                        CheckStatsCommand.createDuelsStatMessage(name, mostRecentDuelsGamemode);
                    } else {
                        CheckStatsCommand.createStatMessage(name, mostRecentGame);
                    }
                } catch (Exception e) {
                    user.addChatMessage(new ChatComponentText("Encountered Error on name" + name + ": " + e.getMessage()));
                }
            }
            statTick = -1;
        }
    }
    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        newWorld = true;
        isGameStarting = false;
    }

    private boolean awaitingLocation = false;
    private boolean hasPolledWorld = false;
    @SubscribeEvent
    public void onWorldChange(WorldEvent.Load event) {
        hasPolledWorld = false;
        awaitingLocation = false;
    }
    private int pollTick = -1;
    @SubscribeEvent //when you join the world, start a one-second timer
    public void onEntityJoin(EntityJoinWorldEvent event) {
        if(event.entity != Minecraft.getMinecraft().thePlayer) return;
        pollTick = 20;
    }
    @SubscribeEvent //tick down the timer and send /locraw after one second
    public void onTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.START) return;
        if(hasPolledWorld) return;
        if(pollTick > 0) {
            pollTick--;
        } else if(pollTick == 0) {
            pollTick = -1;

            Minecraft.getMinecraft().thePlayer.sendChatMessage("/locraw");
            awaitingLocation = true;
            hasPolledWorld = true;
        }
    }
    @SubscribeEvent
    public void determineLocation(ClientChatReceivedEvent event) {
        if(!awaitingLocation) return;
        if(event.message == null) return;
        final String jsonString = event.message.getUnformattedText();
        //sketchy solution may not work
        if(!jsonString.startsWith("{")) return;

        final JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        final boolean isInGame = jsonObject.get("gametype") != null;
        mostRecentGame = isInGame ? jsonObject.get("gametype").getAsString().toLowerCase() : "lobby";
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Detected game: " + mostRecentGame));

        //try to detect a duels gamemode
        final String duelsGameMode = getDuelsGamemode(jsonObject);
        if(mostRecentGame.equals("duels") && duelsGameMode != null)
            mostRecentDuelsGamemode = duelsGameMode;
        awaitingLocation = false;
        event.setCanceled(true);
    }
    private String getDuelsGamemode(JsonObject obj) {
        if(obj.get("mode") == null) return null;
        final String gamemode = obj.get("mode").getAsString().toLowerCase();
        for(String mode : Util.duelsModes)
            if(gamemode.contains(mode)) return mode;
        return null;
    }
}
