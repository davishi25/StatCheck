package com.github.davishi25.statcheck.commands.impl

import com.github.davishi25.statcheck.API
import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.utils.Utils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object ApiSetCommand: CommandBase() {
    override fun getCommandName() = "api"

    override fun getCommandUsage(sender: ICommandSender) = ""

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        when {
            args.size == 1 && args[0] == "clear" -> {
                API.getRecentCalls().clear()
                Utils.modMessage("Successfully cleared stored API responses!")
            }
            args.size == 2 && args[0] == "set" -> {
                Config.setKey(args[1])
                Utils.modMessage("Successfully set API key to ${args[1]}")
            }
            else -> sendHelpMessage()
        }
    }
    fun sendHelpMessage() = Utils.modMessage("Usage:\n/api set {key}\n§7sets api key§r\n/api clear\n§7clears stored api calls§r")

    override fun canCommandSenderUseCommand(sender: ICommandSender) = true
}
