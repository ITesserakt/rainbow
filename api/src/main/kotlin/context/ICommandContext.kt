package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.User
import kotlinx.coroutines.Deferred

interface ICommandContext {
    val client: DiscordClient
    val message: Message
    val author: User
    val commandArgs: Array<String>
    val channel: Deferred<MessageChannel>
}
