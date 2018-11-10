package core.types

import core.ICommandContext

interface ITypeResolver<T> {
    fun read(context : ICommandContext, input: String) : T
}