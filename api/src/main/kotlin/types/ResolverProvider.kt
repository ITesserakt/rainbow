package types

import discord4j.core.`object`.entity.User
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object ResolverProvider {
    @JvmStatic
    private val map = hashMapOf<KClass<*>, ITypeResolver<*>>(
            User::class to UserResolver(),
            Int::class to IntResolver(),
            Long::class to LongResolver(),
            Short::class to ShortResolver(),
            Byte::class to ByteResolver(),
            Float::class to FloatResolver(),
            Double::class to DoubleResolver(),
            String::class to StringResolver(),
            Char::class to CharResolver(),
            Boolean::class to BooleanResolver()
    )

    @JvmStatic
    internal inline operator fun <reified T> getValue(thisRef : Any?, property: KProperty<*>): ITypeResolver<T> {
        val type = T::class
        val resolver = map[type]

        if (resolver != null)
            return resolver as ITypeResolver<T>
        throw NoSuchElementException("Нет подходящего парсера для ${type.qualifiedName}")
    }

    @JvmStatic
    fun <T : Any> bind(pair : Pair<ITypeResolver<T>, KClass<T>>): ResolverProvider {
        map[pair.second] = pair.first
        return this
    }

    @JvmStatic
    internal inline fun <reified T> get(): ITypeResolver<T> = ResolverProvider.getValue(this, this::map)

    @JvmStatic
    internal fun <T : Any> get(type : KClass<T>) : ITypeResolver<T> {
        val resolver = map[type]

        if (resolver != null)
            return resolver as ITypeResolver<T>
        throw NoSuchElementException("Нет подходящего элемента для ${type.qualifiedName}")
    }
}