package ru.tesserakt.bot.rainbow.core.types

import discord4j.core.`object`.entity.Role
import reactor.core.publisher.switchIfEmpty
import ru.tesserakt.bot.rainbow.core.context.GCommandContext
import ru.tesserakt.bot.rainbow.core.context.ICommandContext
import ru.tesserakt.bot.rainbow.util.getRoleById
import ru.tesserakt.bot.rainbow.util.getRolesByName

class RoleResolver : ITypeResolver<Role> {
    override fun read(context: ICommandContext, input: String): Role {
        require(context is GCommandContext) { "Неверный тип контекста" }

        return if (input.startsWith('<') && input.endsWith('>'))
            context.guild.flatMap {
                it.getRoleById(input.substring(3, input.length - 1))
            }.block()!!
        else if (input.all { it.isDigit() })
            context.guild.flatMap {
                it.getRoleById(input)
            }.block()!!
        else
            context.guild.flatMap {
                it.getRolesByName(input).next()
            }.switchIfEmpty {
                throw NoSuchElementException("Не найдено ни одной подходящей роли!")
            }.block()!!
    }
}