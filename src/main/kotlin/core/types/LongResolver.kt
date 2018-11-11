package core.types

import core.ICommandContext

internal class LongResolver : NumberResolver<Long>() {
    override fun read(context: ICommandContext, input: String): Long =
            super.read(context, input.toLongOrNull())
}