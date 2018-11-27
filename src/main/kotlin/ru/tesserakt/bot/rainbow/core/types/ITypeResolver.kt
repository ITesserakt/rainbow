package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext

interface ITypeResolver<T> {
    fun read(context : ICommandContext, input: String) : T

    fun readToEnd(context: ICommandContext, input: Array<String>) : T {
        val stringArgs = input.joinToString(" ") { it }
        return read(context, stringArgs)
    }
}