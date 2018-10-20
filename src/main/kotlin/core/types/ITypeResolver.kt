package core.types

import core.commands.CommandContext

interface ITypeResolver<T> {
    fun read(context : CommandContext, input: String) : T
}