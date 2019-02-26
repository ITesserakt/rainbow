package types

import context.ICommandContext
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class StringResolver : ITypeResolver<String> {
    override fun read(context: ICommandContext, input: String): Mono<String> = input.toMono()
}

