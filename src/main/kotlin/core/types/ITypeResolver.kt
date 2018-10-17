package core.types

import core.commands.ICommandContext

interface ITypeResolver<T> {
    fun read(context : ICommandContext, input: String) : T
}