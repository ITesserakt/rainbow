package core.types

import core.ICommandContext

internal class BooleanResolver : ITypeResolver <Boolean>{
    override fun read(context: ICommandContext, input: String): Boolean = input.toBoolean()
}