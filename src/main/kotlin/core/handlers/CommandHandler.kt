package core.handlers

import core.ICommandContext
import core.Prefix
import core.commands.CommandContext
import core.commands.CommandService
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.PermissionUtils
import java.util.*

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
        val commandByAlias = CommandService.getCommandByAlias(cmdStr)

        try {
            if (commandByName != null && checkPermissions(commandByName.restrictions, context)) {
                commandByName.action(context)
            } else if (commandByAlias != null && checkPermissions(commandByAlias.restrictions, context)) {
                commandByAlias.action(context)
            }
        } catch (ex: Exception) {
            context.channel.sendMessage(ex.localizedMessage)
        }
    }

    private fun checkPermissions(perms: Array<out Permissions>, context: ICommandContext): Boolean {
        if (perms.isEmpty())
            return true
        if (PermissionUtils.hasPermissions(context.guild, context.user, perms.toEnumSet()))
            return true
        else throw IllegalAccessException("Доступ запрещен")
    }

    private fun <T : Enum<T>?> Array<out T>.toEnumSet(): EnumSet<T> = EnumSet.copyOf(this.toList())
}