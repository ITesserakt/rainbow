package types

import context.ICommandContext
import reactor.core.publisher.Mono

interface ITypeResolver <T> {
    fun read(context : ICommandContext, input : String) : Mono<T>

    fun readToEnd(context: ICommandContext, input: List<String>): Mono<T> {
        val stringArgs = input.joinToString(" ")
        return read(context, stringArgs)
    }
}