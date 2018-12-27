@file:Suppress("UNCHECKED_CAST")

package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext
import kotlin.reflect.KClass

object ResolverService {
    private val map = HashMap<KClass<*>, ITypeResolver<*>>()

    /**
     * Сохраняет [resolver] для определённого класса
     */
    fun bind(resolver: Pair<ITypeResolver<*>, KClass<*>>): ResolverService {
        map[resolver.second] = resolver.first
        return this
    }

    /**
     * Возвращает объект [ITypeResolver]
     * @throws [NoSuchElementException]
     */
    private fun <T : Any> getForType(clazz: KClass<T>): ITypeResolver<T> {
        val resolver = map[clazz]
        if (resolver != null)
            return resolver as ITypeResolver<T>
        throw NoSuchElementException("Нет подходящего элемента для ${clazz.qualifiedName}")
    }

    internal fun <T : Any> parse(clazz: KClass<T>, context: ICommandContext, argPos: Int, isRemainder: Boolean = false): T {
        val resolver = getForType(clazz)
        val args = context.args
        val mulArgs = args.drop(argPos).toTypedArray()

        if (mulArgs.isEmpty()) throw NullPointerException("Пропущены параметры с ${argPos + 1} места")

        return if (isRemainder)
            resolver.readToEnd(context, mulArgs)
        else
            resolver.read(context, args.getOrNull(argPos)
                    ?: throw NullPointerException("Пропущен параметр на ${argPos + 1} месте"))
    }

    internal fun <T : Any> parseOptional(clazz: KClass<T>, context: ICommandContext, argPos: Int, isRemainder: Boolean = false): T? =
            if (context.args.size > argPos)
                parse(clazz, context, argPos, isRemainder)
            else null
}