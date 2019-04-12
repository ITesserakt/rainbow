package types.guild

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.find
import kotlinx.coroutines.reactive.openSubscription
import types.MentionableResolver
import util.awaitOrNull
import util.get
import util.toSnowflake

class MemberResolver : MentionableResolver<Member>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await()
            .getMemberById(input[2 until input.length - 1].toSnowflake())
            .awaitOrNull()
    }

    override fun idMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await()
            .getMemberById(input.toSnowflake())
            .awaitOrNull()
    }

    @ObsoleteCoroutinesApi
    override fun elseMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await()
            .members.openSubscription()
            .find { it.nickname.orElse(it.username) == input }
    }

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}