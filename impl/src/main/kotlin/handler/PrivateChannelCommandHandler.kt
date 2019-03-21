package handler

import command.PrivateChannelCommandProvider
import context.PrivateChannelCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.toMono
import util.toOptional

class PrivateChannelCommandHandler : CommandHandler() {
    override fun handle(event: MessageCreateEvent) {
        event.toMono()
                .filter { !it.guildId.isPresent }
                .filter { it.message.content.isPresent }
                .map { it.message.content.get().split(' ') }
                .map { content ->
                    val args = content.drop(1)
                    val context = PrivateChannelCommandContext(event, args)
                    val command = PrivateChannelCommandProvider.find(content[0]).toOptional()
                    context to command
                }.filter { (_, command) -> command.isPresent }
                .map { it.first to it.second.get() }
                .map { (context, command) -> execute(command, context) }
                .subscribe()
    }
}
