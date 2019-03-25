package types

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import util.awaitMany
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

    override fun elseMatchAsync(context: ICommandContext, input: String) = GlobalScope.async {
        context as GuildCommandContext
        context.guild.await()
            .members.awaitMany()
            .find { it.nickname.orElse(it.username) == input }
    }

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}