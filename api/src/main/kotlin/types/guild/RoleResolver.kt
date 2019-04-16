package types.guild

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Role
import getRoleByIdAsync
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.awaitFirstOrNull
import types.MentionableResolver
import util.get
import util.toSnowflake

class RoleResolver : MentionableResolver<Role>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<Role?> = GlobalScope.async {
        require(context is GuildCommandContext)
        context.guild.await().getRoleByIdAsync(input[3..input.length - 2].toSnowflake())
    }

    override fun idMatchAsync(context: ICommandContext, input: String): Deferred<Role?> = GlobalScope.async {
        require(context is GuildCommandContext)
        context.guild.await().getRoleByIdAsync(input.toSnowflake())
    }

    override fun elseMatchAsync(context: ICommandContext, input: String): Deferred<Role?> = GlobalScope.async {
        require(context is GuildCommandContext)
        context.guild.await().roles
            .filter { it.name == input }
            .awaitFirstOrNull()
    }

    override suspend fun read(context: ICommandContext, input: String): Role {
        return when {
            Regex("""^<..\d{18}>$""").matches(input) -> mentionMatchAsync(context, input)
            Regex("""\d{18}""").matches(input) -> idMatchAsync(context, input)
            else -> elseMatchAsync(context, input)
        }.await() ?: throw NoSuchElementException(exceptionMessage)
    }

    override val exceptionMessage: String = "Не найдено ни одной подходящей роли"
}