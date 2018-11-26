package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.console.ConsoleCommandContext
import ru.tesserakt.bot.rainbow.core.console.ConsoleService

@Suppress("FunctionName")
class ConsoleHandler : Handler() {
    fun OnConsoleCommandReceived(context: ConsoleCommandContext) {
        val cmdStr = context.message

        val command = getCommand(ConsoleService, cmdStr) ?: return

        try {
            runCommand(command, context)
        } catch (ex: Exception) {
            context.channel.sendMessage("${ex.localizedMessage}\n${ex.suppressed}\n${ex.stackTrace.joinToString { it.toString() }}")
        }
    }
}