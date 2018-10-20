package core.types

import kotlin.reflect.KClass

object ResolverService {
    val map = HashMap<KClass<*>, ITypeResolver<*>>()

    fun bind(resolver : ITypeResolver<*>, with : KClass<*>) : ResolverService {
        map[with] = resolver
        return this
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getForType() : ITypeResolver<T> {
        val resolver = map[T::class]
        if (resolver != null)
            return resolver as ITypeResolver<T>
        throw NoSuchElementException("Подходящий объект для класса ${T::class.qualifiedName} не найден")
    }
}