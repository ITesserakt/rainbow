package handler

import context.ICommandContext
import types.ITypeResolver
import types.ResolverProvider
import kotlin.reflect.KClass

internal class Parser(private val context: ICommandContext) {
    internal suspend fun <T : Any> parseOptional(index: Int, type: KClass<T>, isContinuous: Boolean): T? = when {
        context.commandArgs.size > index -> parse(index, type, isContinuous)
        else -> null
    }

    internal suspend fun <T : Any> parse(index: Int, type: KClass<T>, isContinuous: Boolean): T {
        val resolver: ITypeResolver<T> = ResolverProvider[type.java]
        val args = context.commandArgs

        val neededArg = args.getOrElse(index) {
            throw NoSuchElementException("Пропущен параметр на ${it + 1} месте")
        }

        return if (isContinuous)
            resolver.readToEnd(context, args.drop(index))
        else
            resolver.read(context, neededArg)
    }
}