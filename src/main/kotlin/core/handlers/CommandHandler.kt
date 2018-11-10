package core.handlers

import core.Prefix
import core.commands.CommandContext
import core.commands.CommandService
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

/**
 * Класс-обработчик поступающих сообщений
 */
@Suppress("FunctionName")
class CommandHandler {

    @EventSubscriber
    fun OnMessageReceived(event: MessageReceivedEvent) {
        val runArray = event.message.content.split(' ')

        if (runArray.isEmpty()) return

        if (runArray[0][0] != Prefix.resolve(event.guild)) return

        val cmdStr = runArray[0].substring(1)
        val argArray = runArray.drop(1).toTypedArray()
        val context = CommandContext(event, argArray)

        val commandByName = CommandService.getCommandByName(cmdStr)

        if (commandByName != null)
            commandByName.action(context)
        else CommandService.getCommandByAlias(cmdStr)?.action?.invoke(context)
    }
}