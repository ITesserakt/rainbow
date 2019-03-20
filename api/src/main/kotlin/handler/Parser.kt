package handler

import context.ICommandContext
import reactor.core.publisher.Mono
import types.ITypeResolver
import types.ResolverProvider
import kotlin.reflect.KClass

internal class Parser(private val context: ICommandContext) {
    internal fun <T : Any> parseOptional(index: Int, type: KClass<T>, isContinuous: Boolean): Mono<T> = when {
        context.commandArgs.size > index -> parse(index, type, isContinuous)
        else -> Mono.empty()
    }

    internal fun <T : Any> parse(index: Int, type: KClass<T>, isContinuous: Boolean): Mono<T> {
        val resolver: ITypeResolver<T> = ResolverProvider[type.java]
        val args = context.commandArgs

        val neededArg = args.getOrNull(index)
                ?: return Mono.error(IllegalArgumentException("Пропущен параметр на ${index + 1} месте"))

        return if (isContinuous)
            resolver.readToEnd(context, args.drop(index))
        else
            resolver.read(context, neededArg)
    }
}