package types

import context.ICommandContext
import reactor.core.publisher.Mono

interface ITypeResolver <T> {
    fun read(context : ICommandContext, input : String) : Mono<T>
}