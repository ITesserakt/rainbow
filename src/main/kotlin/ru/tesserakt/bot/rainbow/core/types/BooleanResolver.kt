package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext

internal class BooleanResolver : ITypeResolver<Boolean> {
    override fun read(context: ICommandContext, input: String): Boolean = input.toBoolean()
}