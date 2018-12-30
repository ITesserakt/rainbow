package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.commands.CommandContext
import ru.tesserakt.bot.rainbow.core.commands.CommandService
import ru.tesserakt.bot.rainbow.core.dropToArray
import ru.tesserakt.bot.rainbow.core.prettyPrint
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

/**
 * Класс-обработчик поступающих сообщений
 */
@Suppress("FunctionName")
class CommandHandler : Handler() {

    @EventSubscriber
    fun OnMessageReceived(event: MessageReceivedEvent) {
        if (!checkForGuildMessage(event)) return

        val runArray = event.message.content.split(' ')
        if (!checkPrefix(runArray)) return

        val cmdStr = runArray[0].drop(1) //Отбрасываем первый символ (префикс) у первого слова
        val argArray = runArray.dropToArray(1) //Отбрасываем первое слово
        val context = CommandContext(event, argArray)

        val command = getCommand(CommandService, cmdStr) ?: return
        try {
            if (checkPermissions(command.restrictions, context)) {
                runCommand(command, context)
            }
        } catch (ex: Exception) {
            context.channel.sendMessage(ex.prettyPrint())
        }
    }

    private fun checkForGuildMessage(event: MessageReceivedEvent): Boolean {
        if (!event.author.isBot && event.guild == null) {
            event.message.channel.sendMessage("Использование бота разрешено только в гильдиях")
            return false
        }
        return true
    }

    private fun checkPrefix(runArray: List<String>): Boolean {
        if ((runArray.isEmpty() || runArray[0].isEmpty()) && runArray[0][0] != '!') return false
        return true
    }
}