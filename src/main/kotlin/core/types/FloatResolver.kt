package core.types

import core.ICommandContext

class FloatResolver : NumberResolver<Float>() {
    override fun read(context: ICommandContext, input: String): Float =
            super.read(context, input.toFloatOrNull())
}