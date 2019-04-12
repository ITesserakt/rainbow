package handler

import command.PrivateChannelCommandProvider
import context.PrivateChannelCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import util.Loggers
import util.isNotPresent

class PrivateChannelCommandHandler : CommandHandler() {
    private val logger = Loggers.getLogger<PrivateChannelCommandHandler>()

    override suspend fun handle(event: MessageCreateEvent) {
        if (event.guildId.isPresent) return
        if (event.member.isPresent) return
        if (event.message.content.isNotPresent) return

        val content = event.message.content.get().split(' ')

        val args = content.drop(1)
        val context = PrivateChannelCommandContext(event, args)
        val command = PrivateChannelCommandProvider.find(content[0]) ?: return

        runCatching { executeAsync(command, context) }
            .onFailure {
                context.channel.await().createMessage(it.localizedMessage).subscribe()
                logger.error(" ", it)
            }
    }
}
