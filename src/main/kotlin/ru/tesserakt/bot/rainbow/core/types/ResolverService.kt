package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.context.ICommandContext
import java.util.*
import kotlin.reflect.KClass

object ResolverService {
    private val resolvers = mutableMapOf<KClass<*>, ITypeResolver<*>>()

    internal fun <T : Any> parseOptional(type: KClass<T>, context: ICommandContext, index: Int, remainder: Boolean): T? =
        if (context.args.size > index)
            parse(type, context, index, remainder)
        else null

    internal fun <T : Any> parse(type: KClass<T>, context: ICommandContext, index: Int, remainder: Boolean): T {
        val resolver = getForType(type)
        val args = context.args
        val mulArgs = args.drop(index).toTypedArray()

        if (mulArgs.isEmpty()) throw NullPointerException("Пропущены параметры с ${index + 1} места")

        return if (remainder)
            resolver.readToEnd(context, mulArgs)
        else
            resolver.read(context, args.getOrNull(index)
                    ?: throw NullPointerException("Пропущен параметр на ${index + 1} месте"))
    }

    private fun <T : Any> getForType(type: KClass<T>): ITypeResolver<T> {
        val resolver = resolvers[type]
        @Suppress("UNCHECKED_CAST")
        if (resolver != null)
            return resolver as ITypeResolver<T>
        throw NoSuchElementException("Нет подходящего элемента для ${type.qualifiedName}")
    }

    fun <T : Any> bind(pair : Pair<ITypeResolver<T>, KClass<T>>) : ResolverService {
        resolvers[pair.second] = pair.first
        return this
    }
}