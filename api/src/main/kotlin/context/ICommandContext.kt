package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message
import discord4j.core.`object`.entity.User
import reactor.core.publisher.Mono

interface ICommandContext {
    val client : DiscordClient
    val message : Message
    val author: Mono<User>
    val commandArgs : Array<String>
}
