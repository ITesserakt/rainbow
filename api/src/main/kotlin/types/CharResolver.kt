package types

import context.ICommandContext
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

class CharResolver : ITypeResolver<Char> {
    override fun read(context: ICommandContext, input: String): Mono<Char> = input[0].toMono()
}