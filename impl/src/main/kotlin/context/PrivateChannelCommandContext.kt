package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class PrivateChannelCommandContext(event: MessageCreateEvent, args: List<String>) : ICommandContext {
    override val client: DiscordClient = event.client
    override val message: Message = event.message
    override val author: Mono<User> = event.message.author.get().toMono()
    override val commandArgs: Array<String> = args.toTypedArray()
}
