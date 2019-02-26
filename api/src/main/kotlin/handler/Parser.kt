package handler

import context.ICommandContext
import reactor.core.publisher.Mono
import types.ResolverProvider
import kotlin.reflect.KClass

internal class Parser(private val context: ICommandContext) {
    internal fun <T : Any> parseOptional(index: Int, type : KClass<T>): Mono<T> = when {
        context.commandArgs.size > index -> parse(index, type)
        else -> Mono.empty()
    }

    internal fun <T : Any> parse(index: Int, type: KClass<T>): Mono<T> {
        val resolver = ResolverProvider.get(type)
        val args = context.commandArgs

        val neededArg = args.getOrNull(index)
                ?: return Mono.error(IllegalArgumentException("Пропущен параметр на ${index + 1} месте"))

        return resolver.read(context, neededArg)
    }
}