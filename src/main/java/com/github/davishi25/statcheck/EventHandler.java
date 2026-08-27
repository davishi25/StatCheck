package com.github.davishi25.statcheck;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {
    private boolean newWorld = false;

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(!newWorld) return;

        newWorld = false;
    }

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        newWorld = true;
    }

}
