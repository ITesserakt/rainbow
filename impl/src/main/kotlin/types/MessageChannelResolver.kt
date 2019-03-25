package types

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.MessageChannel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.awaitFirstOrNull
import reactor.core.publisher.cast
import util.awaitOrNull
import util.toSnowflake

class MessageChannelResolver : MentionableResolver<MessageChannel>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<MessageChannel?> =
        GlobalScope.async {
            (context as GuildCommandContext)
            context.guild.await()
                .getChannelById(input.substring(2, input.length - 1).toSnowflake())
                .cast<MessageChannel>()
                .awaitOrNull()
        }

    override fun idMatchAsync(context: ICommandContext, input: String): Deferred<MessageChannel?> = GlobalScope.async {
        (context as GuildCommandContext)
        context.guild.await()
            .getChannelById(input.toSnowflake())
            .cast<MessageChannel>()
            .awaitOrNull()
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