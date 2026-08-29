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
            Config.setKey("");
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Successfully cleared API key"));
        } else if(args.length == 2 && args[0].equals("set")) {
            Config.setKey(args[1]);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Successfully set API key to " + args[1]));

        } else {
            sendHelpMessage();
        }
    }
    public void sendHelpMessage() {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Usage:\n/api set {key}\n/api clear"));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }
}
