package core.types

import core.ICommandContext

interface ITypeResolver<T> {
    fun read(context : CommandContext, input: String) : T

    fun readToEnd(context: CommandContext, input: Array<String>) : T {
        val stringArgs = input.joinToString(" ") { it }
        return read(context, stringArgs)
    }
}