package core.types

import core.ICommandContext

internal class LongResolver : ITypeResolver<Long> {
    override fun read(context: ICommandContext, input: String): Long = input.toLong()
}