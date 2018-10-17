package core.commands

import core.resolvePrefix
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent

@Suppress("FunctionName")
class CommandHandler {

    @EventSubscriber
    fun OnMessageReceived(event: MessageReceivedEvent) {
        val runArray = event.message.content.split(' ')

        if (runArray.isEmpty()) return

//        if(runArray[0][1] != resolvePrefix(event.guild)) return

        val cmdStr = runArray[0].substring(1)
        val argArray = runArray.subList(1, runArray.size).toTypedArray()
        val context = CommandContext(event, argArray)

        val commandByName = CommandService.getCommandByName(cmdStr)

        if(commandByName != null)
            commandByName.execute(context)
        else CommandService.getCommandByAlias(cmdStr)?.execute(context)
    }
}