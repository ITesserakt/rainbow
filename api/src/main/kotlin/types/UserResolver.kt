package types

import context.ICommandContext
import discord4j.core.`object`.entity.User
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import util.awaitMany
import util.awaitOrNull
import util.toSnowflake

class UserResolver : MentionableResolver<User>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<User?> = GlobalScope.async {
        context.client
            .getUserById(input.substring(2, input.length - 1).toSnowflake())
            .awaitOrNull()
    }

    override fun idMatchAsync(context: ICommandContext, input: String): Deferred<User?> = GlobalScope.async {
        context.client
            .getUserById(input.toSnowflake())
            .awaitOrNull()
    }

    override fun elseMatchAsync(context: ICommandContext, input: String): Deferred<User?> = GlobalScope.async {
        val users = context.client.users.awaitMany()
        val normalName = input.split('#')
            .takeIf { it.size == 2 } ?: throw  IllegalArgumentException("Ожидалось `Name#ID`, получено `$input`")

        users.find { it.username == normalName[0] && it.discriminator == normalName[1] }
    }

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}