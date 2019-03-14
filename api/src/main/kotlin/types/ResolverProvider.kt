package types

import discord4j.core.`object`.entity.User
import kotlin.reflect.KProperty

object ResolverProvider {
    val resolversMap = hashMapOf<Class<*>, ITypeResolver<*>>(
            User::class.java to UserResolver(),
            Int::class.java to IntResolver(),
            Long::class.java to LongResolver(),
            Short::class.java to ShortResolver(),
            Byte::class.java to ByteResolver(),
            Float::class.java to FloatResolver(),
            Double::class.java to DoubleResolver(),
            String::class.java to StringResolver(),
            Char::class.java to CharResolver(),
            Boolean::class.java to BooleanResolver()
    )

    inline operator fun <reified T> getValue(ref: Nothing?, property: KProperty<*>): ITypeResolver<T> =
            ResolverProvider.get<T>() as ITypeResolver<T>

    inline fun <reified T> get(): ITypeResolver<*> = resolversMap.getOrElse(T::class.java) {
        throw NoSuchElementException("Нет подходящего парсера для ${T::class.qualifiedName}")
    }

    inline fun <reified T : Any> bind() = T::class.java

    infix fun <T : Any> Class<T>.with(resolver: ITypeResolver<T>) {
        resolversMap[this] = resolver
    }

    operator fun <T : Any> get(type: Class<T>): ITypeResolver<T> = resolversMap.getOrElse(type) {
        throw NoSuchElementException("Нет подходящего парсера для ${type.simpleName}")
    } as ITypeResolver<T>
}

fun resolverProvider(init: ResolverProvider.() -> Unit) {
    ResolverProvider.init()
}