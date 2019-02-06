package ru.tesserakt.bot.rainbow.core.types

import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake
import reactor.core.publisher.switchIfEmpty
import ru.tesserakt.bot.rainbow.core.context.ICommandContext

class UserResolver : ITypeResolver<User> {
    override fun read(context: ICommandContext, input: String): User {
        return if (input.startsWith('<') && input.endsWith('>')) {
            context.client.getUserById(Snowflake.of(input.substring(2, input.length - 1))).block()!!
        } else if (input.all { it.isDigit() }) {
            context.client.getUserById(Snowflake.of(input)).block()!!
        } else {
            context.client.users
                    .filter { it.username == input }.next()
                    .switchIfEmpty {
                        throw NoSuchElementException("Не найдено ни одного подходящего пользователя")
                    }.block()!!
        }
    }
}