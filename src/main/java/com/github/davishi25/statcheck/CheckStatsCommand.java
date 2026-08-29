package com.github.davishi25.statcheck;

import com.github.davishi25.statcheck.parser.ApiParser;
import com.github.davishi25.statcheck.parser.BridgeDuelsParser;
import com.github.davishi25.statcheck.parser.DuelsParser;
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
        } else if(args.length == 3) {
            if(args[1].equals("d") || args[1].equals("duels"))
                createDuelsStatMessage(args[0],args[2]);
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
                user.addChatMessage(new ChatComponentText("§c[SC] Error: ").appendText(e.getMessage()));
                System.err.println(e);
            }
        });
        thread.start();
    }
    static String checkStats(JsonObject playerObj, ApiParser parser) {
        return parser.getStatLine(playerObj);
    }

    static void createDuelsStatMessage(String player, String duelsMode) {
        Thread thread = new Thread(() -> {
            final EntityPlayerSP user = Minecraft.getMinecraft().thePlayer;
            try {
                JsonObject apiResponse = API.getAPI(player);
                ApiParser parser;
                if(duelsMode.equals("bridge")) {
                    parser = ApiParserRegistry.parsers.get("bridge");
                } else {
                    parser = ApiParserRegistry.parsers.get("duels");
                }

                final String padding = "§8----------------------------------------";
                final String nameLine = Util.getFormattedName(apiResponse) + "'s " + duelsMode + " duels§r\n";
                final String statLine = checkDuelsStats(apiResponse.getAsJsonObject("stats"),parser,duelsMode);

                String message = padding + "\n§8| " + nameLine + "§8| " + statLine + "\n" + padding;
                user.addChatMessage(new ChatComponentText(message));
            } catch (Exception e) {
                user.addChatMessage(new ChatComponentText("§c[SC] Error: ").appendText(e.getMessage()));
                System.err.println(e);
            }
        });
        thread.start();
    }
    static String checkDuelsStats(JsonObject playerObj, ApiParser parser, String gamemode) {
        if(parser instanceof BridgeDuelsParser)
            return parser.getStatLine(playerObj);
        return ((DuelsParser)parser).getStatLine(gamemode,playerObj);
    }

}
