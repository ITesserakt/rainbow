package core.types

import core.ICommandContext

class FloatResolver : ITypeResolver<Float> {
    override fun read(context: ICommandContext, input: String): Float = input.toFloat()
}