package core.handlers

import core.console.ConsoleCommandContext
import core.console.ConsoleService

@Suppress("FunctionName")
class ConsoleHandler {
    fun OnConsoleCommandReceived(context: ConsoleCommandContext) {
        val cmdStr = context.message
        val commandByName = ConsoleService.getCommandByName(cmdStr)

        try {
            if (commandByName != null)
                commandByName.action(context)
            else {
                val commandByAlias = ConsoleService.getCommandByAlias(cmdStr)
                commandByAlias?.action?.invoke(context)
            }
        } catch (ex: Exception) {
            context.channel.sendMessage(ex.localizedMessage)
        }
    }
}