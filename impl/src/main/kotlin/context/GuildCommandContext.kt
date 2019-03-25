package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import util.await

class GuildCommandContext(event: MessageCreateEvent, args: List<String>) : ICommandContext {
    override val channel: Deferred<MessageChannel> = GlobalScope.async {
        event.message.channel.await()
    }
    override val client: DiscordClient = event.client
    override val message: Message = event.message
    override val author: User = event.message.author.get()
    override val commandArgs: Array<String> = args.toTypedArray()

    val guild: Deferred<Guild> = GlobalScope.async { event.guild.await() }
    val guildId: Snowflake = event.guildId.get()
}