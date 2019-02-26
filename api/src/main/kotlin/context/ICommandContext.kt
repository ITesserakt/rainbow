package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Message
import reactor.core.publisher.Mono

interface ICommandContext {
    val client : DiscordClient
    val message : Message
    val member : Mono<Member>
    val commandArgs : Array<String>
}
