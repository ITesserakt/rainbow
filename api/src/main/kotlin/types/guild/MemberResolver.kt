package types.guild

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import getMemberByIdAsync
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.reactive.flow.asFlow
import types.MentionableResolver
import util.get
import util.toSnowflake

class MemberResolver : MentionableResolver<Member>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await().getMemberByIdAsync(input[2 until input.length - 1].toSnowflake())
    }

    override fun idMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await().getMemberByIdAsync(input.toSnowflake())
    }

    @ObsoleteCoroutinesApi
    override fun elseMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await()
            .members.asFlow()
            .filter { it.nickname.orElse(it.username) == input }
            .singleOrNull()
    }

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}