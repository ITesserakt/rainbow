package types

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.entity.Role
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import util.toSnowflake

class RoleResolver : MentionableResolver<Role>() {
    override fun mentionMatch(context: ICommandContext, input: String): Mono<Role> =
            (context as GuildCommandContext)
                    .guild.flatMap { it.getRoleById(input.substring(3, input.length - 1).toSnowflake()) }

    override fun idMatch(context: ICommandContext, input: String): Mono<Role> =
            (context as GuildCommandContext)
                    .guild.flatMap { it.getRoleById(input.toSnowflake()) }

    override fun elseMatch(context: ICommandContext, input: String): Mono<Role> =
            (context as GuildCommandContext)
                    .guild.flatMapMany { it.roles }
                    .filter { it.name == input }
                    .next()

    override fun read(context: ICommandContext, input: String): Mono<Role> {
        return when {
            Regex("""^<..\d{18}>$""").matches(input) -> mentionMatch(context, input)
            Regex("""\d{18}""").matches(input) -> idMatch(context, input)
            else -> elseMatch(context, input)
        }.switchIfEmpty { throw NoSuchElementException(exceptionMessage) }
    }

    override val exceptionMessage: String = "Не найдено ни одной подходящей роли"
}