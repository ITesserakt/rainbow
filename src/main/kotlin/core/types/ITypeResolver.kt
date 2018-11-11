package core.types

import core.ICommandContext

interface ITypeResolver<T> {
    fun read(context : ICommandContext, input: String) : T

    fun readToEnd(context: ICommandContext, input: Array<String>) : T {
        val stringArgs = input.joinToString(" ") { it }
        return read(context, stringArgs)
    }
}