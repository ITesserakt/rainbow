package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.context.ICommandContext

class StringResolver : ITypeResolver<String> {
    override fun read(context: ICommandContext, input: String): String = input
}