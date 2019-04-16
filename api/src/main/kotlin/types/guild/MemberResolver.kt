package types.guild

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import getMemberByIdAsync
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.awaitFirstOrNull
import types.MentionableResolver
import util.get
import util.toSnowflake

class MemberResolver : MentionableResolver<Member>() {
    override fun mentionMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        require(context is GuildCommandContext)
        context.guild.await().getMemberByIdAsync(input[2 until input.length - 1].toSnowflake())
    }

    override fun idMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        require(context is GuildCommandContext)
        context.guild.await().getMemberByIdAsync(input.toSnowflake())
    }

    @ObsoleteCoroutinesApi
    override fun elseMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        require(context is GuildCommandContext)
        context.guild.await().members
            .filter { it.nickname.orElse(it.username) == input }
            .awaitFirstOrNull()
    }

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}