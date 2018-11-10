package core.types

import core.ICommandContext

class StringResolver : ITypeResolver<String> {
    override fun read(context: ICommandContext, input: String): String = input
}