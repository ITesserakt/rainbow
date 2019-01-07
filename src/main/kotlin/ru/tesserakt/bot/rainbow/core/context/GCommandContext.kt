package ru.tesserakt.bot.rainbow.core.context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Message
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono

class GCommandContext (event : MessageCreateEvent, override val args: List<String>) : ICommandContext {
    override val message: Message = event.message
    override val client: DiscordClient = event.client
    val guild: Mono<Guild> = event.guild
}