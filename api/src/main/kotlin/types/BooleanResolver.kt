package types

import context.ICommandContext
import reactor.core.publisher.toMono

class BooleanResolver : ITypeResolver<Boolean> {
    override fun read(context: ICommandContext, input: String) = input.toBoolean().toMono()
}