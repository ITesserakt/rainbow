package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.PrivateChannel
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import util.await

class PrivateChannelCommandContext(event: MessageCreateEvent, args: List<String>) : ICommandContext {
    override val channel: Deferred<PrivateChannel> = GlobalScope.async {
        event.message.channel.await() as PrivateChannel
    }
    override val client: DiscordClient = event.client
    override val message: Message = event.message
    override val author: User = event.message.author.get()
    override val commandArgs: Array<String> = args.toTypedArray()
}
