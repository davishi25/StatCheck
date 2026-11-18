package com.github.davishi25.statcheck.commands

import com.github.davishi25.statcheck.commands.impl.ApiSetCommand
import com.github.davishi25.statcheck.commands.impl.CheckStatsCommand
import com.github.davishi25.statcheck.commands.impl.DebugCommand
import com.github.davishi25.statcheck.commands.impl.RequeueCommand
import net.minecraftforge.client.ClientCommandHandler

object Commands {
    val commands = listOf(ApiSetCommand, CheckStatsCommand, DebugCommand, RequeueCommand)

    fun registerCommands() = commands.forEach { command ->
        ClientCommandHandler.instance.registerCommand(command)
    }
}