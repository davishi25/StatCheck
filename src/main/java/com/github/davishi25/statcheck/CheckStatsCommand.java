package com.github.davishi25.statcheck;

import com.github.davishi25.statcheck.parser.ApiParser;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
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
        final EntityPlayerSP user = Minecraft.getMinecraft().thePlayer;
        if(args.length == 0) {
            createStatMessage(user.getName(),"");
        } else if(args.length == 1) {
            user.addChatMessage(new ChatComponentText("§cError, specify a gamemode"));
        } else if(args.length == 2){
            createStatMessage(args[0],args[1]);
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) { return true; }

    static void createStatMessage(String player, String game) {
        Thread thread = new Thread(() -> {
            final EntityPlayerSP user = Minecraft.getMinecraft().thePlayer;
            try {
                JsonObject apiResponse = API.getAPI(player);
                ApiParser parser = ApiParserRegistry.parsers.get(game);
                if(parser == null) throw new NullPointerException("Couldn't find gamemode \"" + game + "\". Did you spell it correctly?");

                final String padding = "§8----------------------------------------";
                final String nameLine = Util.getNameLine(apiResponse, parser);
                final String statLine = checkStats(apiResponse.getAsJsonObject("stats"),parser);

                String message = padding + "\n§8| " + nameLine + "§8| " + statLine + "\n" + padding;
                user.addChatMessage(new ChatComponentText(message));
            } catch (Exception e) {
                user.addChatMessage(new ChatComponentText("§c§l[SC] Error: §r§c").appendText(e.getCause().getMessage()));
                System.out.println(e);
                throw e;
            }
        });
        thread.start();
    }

    static String checkStats(JsonObject playerObj, ApiParser parser) {
        return parser.getStatLine(playerObj);
    }
}
