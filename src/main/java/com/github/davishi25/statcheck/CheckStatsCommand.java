package com.github.davishi25.statcheck;

import com.github.davishi25.statcheck.parser.ApiParser;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;

public class CheckStatsCommand extends CommandBase {
    @Override
    public String getCommandName() { return "sc"; }

    @Override
    public String getCommandUsage(ICommandSender sender) { return ""; }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        StatCheck.user = Minecraft.getMinecraft().thePlayer;
        if(args.length == 0) {
            createStatMessage(StatCheck.user.getName(),"");
        } else if(args.length == 1) {
            StatCheck.user.addChatMessage(new ChatComponentText("§cError, specify a gamemode"));
        } else if(args.length == 2){
            createStatMessage(args[0],args[1]);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }

    static void createStatMessage(String player, String game) {
        Thread thread = new Thread(() -> {
            try {
                JsonObject apiResponse = API.getAPI(player);
                ApiParser parser = Util.parsers.get(game);
                if(parser == null) throw new NullPointerException("Couldn't find gamemode \"" + game + "\". Did you spell it correctly?");

                final String padding = "§8----------------------------------------";
                final String nameLine = Util.getNameLine(apiResponse, parser);
                final String statLine = checkStats(apiResponse.getAsJsonObject("stats"),parser);

                String message = padding + "\n" + nameLine + statLine + "\n" + padding;
                StatCheck.user.addChatMessage(new ChatComponentText(message));
            } catch (Exception e) {
                StatCheck.user.addChatMessage(new ChatComponentText("§cEncountered an error: ").appendText(e.toString()));
                System.out.println(e);
            }
        });
        thread.start();
    }

    static String checkStats(JsonObject playerObj, ApiParser parser) {
        return parser.getStatLine(playerObj);
    }
}
