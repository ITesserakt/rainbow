package types.guild

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.MessageChannel
import getChannelByIdAsync
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.awaitFirstOrNull
import reactor.core.publisher.cast
import types.MentionableResolver
import util.get
import util.toSnowflake

class MessageChannelResolver : MentionableResolver<MessageChannel>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<MessageChannel?> =
        GlobalScope.async {
            (context as GuildCommandContext)
            context.guild.await().getChannelByIdAsync(input[2 until input.length - 1].toSnowflake()) as MessageChannel
        }

    override fun idMatchAsync(context: ICommandContext, input: String): Deferred<MessageChannel?> = GlobalScope.async {
        (context as GuildCommandContext)
        context.guild.await().getChannelByIdAsync(input.toSnowflake()) as MessageChannel
    }

    override fun elseMatchAsync(context: ICommandContext, input: String): Deferred<MessageChannel?> =
        GlobalScope.async {
            (context as GuildCommandContext)
                .client.guilds.flatMap { it.channels }
                .filter { it.name == input }
                .cast<MessageChannel>()
                .awaitFirstOrNull()
        }

    override val exceptionMessage: String = "Не найдено ни одного подходящего канала"
}