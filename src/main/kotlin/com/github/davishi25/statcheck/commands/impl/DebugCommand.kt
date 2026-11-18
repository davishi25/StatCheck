package com.github.davishi25.statcheck.commands.impl

import com.github.davishi25.statcheck.config.Config
import com.github.davishi25.statcheck.utils.Utils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

object DebugCommand: CommandBase() {
    override fun getCommandName() = "scdebug"

    override fun getCommandUsage(sender: ICommandSender) = ""

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        Config.debugMode = !Config.debugMode
        Utils.modMessage("debugMode set to ${Config.debugMode}")
        Config.config?.let { cfg ->
            val debugProperty = cfg.get(Config.CATEGORY_GENERAL,"debugMode",false)
            debugProperty.set(Config.debugMode)
            cfg.save()
        }
    }

    override fun canCommandSenderUseCommand(sender: ICommandSender) = true
}