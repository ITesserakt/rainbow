package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.Prefix
import ru.tesserakt.bot.rainbow.core.commands.CommandContext
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

/**
 * Класс-обработчик поступающих сообщений
 */
@Suppress("FunctionName")
class CommandHandler : Handler() {

    @EventSubscriber
    fun OnMessageReceived(event: MessageReceivedEvent) {
        val runArray = event.message.content.split(' ')

        if (runArray.isEmpty()) return

        if (runArray[0][0] != Prefix.resolve(event.guild)) return

        val cmdStr = runArray[0].substring(1)
        val argArray = runArray.drop(1).toTypedArray()
        val context = CommandContext(event, argArray)

        val command = getCommand(cmdStr) ?: return

        try {
            if (checkPermissions(command.restrictions, context)) {
                runCommand(command, context)
            }
        } catch (ex: Exception) {
            context.channel.sendMessage("${ex.localizedMessage}\n${ex.suppressed}\n${ex.stackTrace.joinToString { it.toString() }}")
        }
    }
}