package ru.tesserakt.bot.rainbow.core.handlers

import org.slf4j.LoggerFactory
import ru.tesserakt.bot.rainbow.core.console.ConsoleCommandContext
import ru.tesserakt.bot.rainbow.core.console.ConsoleService

@Suppress("FunctionName")
class ConsoleHandler : Handler() {
    fun OnConsoleCommandReceived(context: ConsoleCommandContext) {
        val cmdStr = context.message
        val logger = LoggerFactory.getLogger(ConsoleHandler::class.java)

        val command = getCommand(ConsoleService, cmdStr) ?: return

        try {
            runCommand(command, context)
        } catch (ex: Exception) {
            logger.error("Ошибка при вызове '${command.name}'", ex)
        }
    }
}