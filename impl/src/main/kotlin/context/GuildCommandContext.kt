package context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class GuildCommandContext(event: MessageCreateEvent, args: List<String>) : ICommandContext {
    override val client: DiscordClient = event.client
    override val message: Message = event.message
    override val member: Mono<Member> = event.member.get().toMono()
    override val commandArgs: Array<String> = args.toTypedArray()
    val guild: Mono<Guild> = event.guild
    val guildId = event.guildId.orElseThrow { IllegalStateException("Такого никогда не должно было произойти, но оно произошло...") }
}