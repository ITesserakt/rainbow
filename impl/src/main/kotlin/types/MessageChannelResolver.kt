package types

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.MessageChannel
import reactor.core.publisher.Mono
import reactor.core.publisher.cast
import util.toSnowflake

class MessageChannelResolver : MentionableResolver<MessageChannel>() {
    override fun mentionMatch(context: ICommandContext, input: String): Mono<MessageChannel> =
        (context as GuildCommandContext)
                .guild.flatMap { it.getChannelById(input.substring(2, input.length - 1).toSnowflake()) }
                .cast()

    override fun idMatch(context: ICommandContext, input: String): Mono<MessageChannel> =
            (context as GuildCommandContext)
                    .guild.flatMap { it.getChannelById(input.toSnowflake()) }
                    .cast()


    override fun elseMatch(context: ICommandContext, input: String): Mono<MessageChannel> =
            (context as GuildCommandContext)
                    .client.guilds.flatMap { it.channels }
                    .filter { it.name == input }
                    .next().cast()

    override val exceptionMessage: String = "Не найдено ни одного подходящего канала"
}