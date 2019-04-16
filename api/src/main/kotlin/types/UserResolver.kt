package types

import context.ICommandContext
import discord4j.core.`object`.entity.User
import getUserByIdAsync
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.reactive.flow.asFlow
import util.toSnowflake

class UserResolver : MentionableResolver<User>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<User?> = GlobalScope.async {
        context.client.getUserByIdAsync(input.substring(2, input.length - 1).toSnowflake())
    }

    override fun idMatchAsync(context: ICommandContext, input: String): Deferred<User?> = GlobalScope.async {
        context.client.getUserByIdAsync(input.toSnowflake())
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun elseMatchAsync(context: ICommandContext, input: String): Deferred<User?> = GlobalScope.async {
        val users = context.client.users.asFlow()
        val normalName = input.split('#')
            .takeIf { it.size == 2 }
            ?: throw  IllegalArgumentException("Ожидалось `Name#ID`, получено `$input`")

        users.filter { it.username == normalName[0] && it.discriminator == normalName[1] }.singleOrNull()
    }

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}