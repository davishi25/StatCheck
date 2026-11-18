package com.github.davishi25.statcheck

import com.github.davishi25.statcheck.utils.Utils.user
import com.github.davishi25.statcheck.utils.StatUtils
import com.github.davishi25.statcheck.utils.Utils
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class CheckStatsCommand: CommandBase() {

    override fun getCommandName(): String = "sc"

    override fun getCommandUsage(sender: ICommandSender) = ""

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        when (args.size) {
            0 -> StatUtils.createStatMessage(user.name,"")
            1 -> Utils.modMessage("§cError, specify a gamemode")
            2 -> StatUtils.createStatMessage(args[0],args[1])
            3 -> {
                if(args[1] == "d" || args[1] == "duels") {
                    val mode = args[2]
                    if (mode in Utils.duelsModes) StatUtils.createDuelsStatMessage(args[0], args[2])
                    else {
                        val supportedGames = Utils.duelsModes.joinToString(" ")
                        Utils.modMessage("§cError: unsupported game \"$mode\"\n§cSupported args: $supportedGames")
                    }
                }
            }
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender): Boolean = true

}
