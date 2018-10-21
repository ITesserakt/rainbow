package core.handlers

import core.commands.CommandContext
import core.commands.CommandService
import core.resolvePrefix
import core.types.EmptyInput
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

        if (runArray[0][0] != resolvePrefix(event.guild)) return

        val cmdStr = runArray[0].substring(1)
        val argArray = runArray.subList(1, runArray.size).toTypedArray()
        val context = CommandContext(event, argArray)

        val commandByName = CommandService.getCommandByName(cmdStr)

        try {
            if (commandByName != null)
                commandByName.action(context)
            else CommandService.getCommandByAlias(cmdStr)?.action?.invoke(context)
        } catch (ex: NullPointerException) {
            context.channel.sendMessage("Ошибка: ${ex.localizedMessage}")
        } catch (ex: EmptyInput) {
            context.channel.sendMessage("Ошибка: ввод не соответствует требованиям")
        } catch (ex: NoSuchElementException) {
            context.channel.sendMessage("Ошибка: подходящий элемент не найден")
        } catch (ex: RuntimeException) {
            context.channel.sendMessage(ex.localizedMessage)
        }
    }
}