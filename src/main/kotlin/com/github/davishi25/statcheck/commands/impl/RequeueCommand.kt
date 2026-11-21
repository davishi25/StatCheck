package com.github.davishi25.statcheck.commands.impl

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.utils.location.LocationUtils
import com.github.davishi25.statcheck.utils.Utils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object RequeueCommand: CommandBase() {
    override fun getCommandName() = "rq"
    override fun getCommandAliases(): List<String> = listOf(
        "rq",
        "requeue"
    )

    override fun getCommandUsage(sender: ICommandSender) = ""

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (Config.debugMode) Utils.modMessage("requeuing for ${LocationUtils.mode}")
        if (LocationUtils.mode == null) {
            Utils.modMessage("§cCould not detect a mode to requeue!")
            return
        }
        Utils.sendMessage("/play ${LocationUtils.mode}")
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender) = true
}