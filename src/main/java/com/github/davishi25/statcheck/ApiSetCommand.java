package com.github.davishi25.statcheck;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ApiSetCommand extends CommandBase {
    @Override
    public String getCommandName() { return "api"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return ""; }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length == 1 && args[0].equals("clear")) {
            API.getRecentCalls().clear();
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Successfully cleared stored API responses!"));
        } else if(args.length == 2 && args[0].equals("set")) {
            Config.setKey(args[1]);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Successfully set API key to " + args[1]));

        } else {
            sendHelpMessage();
        }
    }
    public void sendHelpMessage() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Usage:\n/api set {key}\n§7sets api key§r\n/api clear\n§7clears stored api calls§r"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
}
