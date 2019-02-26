package types

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import reactor.core.publisher.Mono
import util.toSnowflake

class MemberResolver : MentionableResolver<Member>() {
    override fun mentionMatch(context: ICommandContext, input: String): Mono<Member> =
            (context as GuildCommandContext)
                    .guild.flatMap { it.getMemberById(input.substring(2, input.length - 1).toSnowflake()) }

    override fun idMatch(context: ICommandContext, input: String): Mono<Member> =
            (context as GuildCommandContext)
                    .guild.flatMap { it.getMemberById(input.toSnowflake()) }

    override fun elseMatch(context: ICommandContext, input: String): Mono<Member> =
            (context as GuildCommandContext)
                    .guild.flatMapMany { it.members }
                    .filter { it.username == input }
                    .next()

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}