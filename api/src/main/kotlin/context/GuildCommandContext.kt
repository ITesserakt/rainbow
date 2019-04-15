package context

import channelAsync
import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.TextChannel
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import guildAsync
import kotlinx.coroutines.Deferred

@Suppress("UNCHECKED_CAST")
class GuildCommandContext(event: MessageCreateEvent, args: List<String>) : ICommandContext {
    override val channel: Deferred<TextChannel> = event.message.channelAsync as Deferred<TextChannel>
    override val client: DiscordClient = event.client
    override val message: Message = event.message
    override val author: User = event.message.author.get()
    override val commandArgs: Array<String> = args.toTypedArray()

    val guild: Deferred<Guild> = event.guildAsync
    val guildId: Snowflake = event.guildId.get()
}