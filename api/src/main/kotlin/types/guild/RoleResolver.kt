package types.guild

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Role
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.find
import kotlinx.coroutines.reactive.openSubscription
import types.MentionableResolver
import util.awaitOrNull
import util.get
import util.toSnowflake

class RoleResolver : MentionableResolver<Role>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<Role?> = GlobalScope.async {
        (context as GuildCommandContext)
        context.guild.await()
            .getRoleById(input[3..input.length - 2].toSnowflake())
            .awaitOrNull()
    }

    override fun idMatchAsync(context: ICommandContext, input: String): Deferred<Role?> = GlobalScope.async {
        (context as GuildCommandContext)
        context.guild.await()
            .getRoleById(input.toSnowflake())
            .awaitOrNull()
    }

    @ObsoleteCoroutinesApi
    override fun elseMatchAsync(context: ICommandContext, input: String): Deferred<Role?> = GlobalScope.async {
        (context as GuildCommandContext)
        context.guild.await()
            .roles.openSubscription()
            .find { it.name == input }
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    override suspend fun read(context: ICommandContext, input: String): Role {
        return when {
            Regex("""^<..\d{18}>$""").matches(input) -> mentionMatchAsync(context, input)
            Regex("""\d{18}""").matches(input) -> idMatchAsync(context, input)
            else -> elseMatchAsync(context, input)
        }.await() ?: throw NoSuchElementException(exceptionMessage)
    }

    override val exceptionMessage: String = "Не найдено ни одной подходящей роли"
}