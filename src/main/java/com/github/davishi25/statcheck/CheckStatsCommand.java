package com.github.davishi25.statcheck;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class CheckStatsCommand extends CommandBase {
    @Override
    public String getCommandName() { return "sc"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return ""; }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        StatCheck.user = Minecraft.getMinecraft().thePlayer;
        if(args.length == 0) {
            StatCheck.checkStats(StatCheck.user.getName(),"");
        } else if(args.length == 1) {
            StatCheck.user.addChatMessage(new ChatComponentText("§cError, specify a gamemode"));
        } else if(args.length == 2){
            StatCheck.checkStats(args[0],args[1]);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }


}
