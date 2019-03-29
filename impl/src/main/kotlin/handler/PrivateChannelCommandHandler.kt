package handler

import command.PrivateChannelCommandProvider
import context.PrivateChannelCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import util.Loggers
import util.isNotPresent

class PrivateChannelCommandHandler : CommandHandler() {
    private val logger = Loggers.getLogger<PrivateChannelCommandHandler>()

    override fun handle(event: MessageCreateEvent) = GlobalScope.launch {
        if (event.guildId.isPresent) return@launch
        if (event.member.isPresent) return@launch
        if (event.message.content.isNotPresent) return@launch

        val content = event.message.content.get().split(' ')

        val args = content.drop(1)
        val context = PrivateChannelCommandContext(event, args)
        val command = PrivateChannelCommandProvider.find(content[0]) ?: return@launch

        runCatching { executeAsync(command, context) }
            .onFailure {
                context.channel.await().createMessage(getError(it)).subscribe()
                logger.error(" ", it)
            }
    }
}
