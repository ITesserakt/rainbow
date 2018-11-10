package core.types

import kotlin.reflect.KClass

object ResolverService {
    val map = HashMap<KClass<*>, ITypeResolver<*>>()

    /**
     * Сохраняет [resolver] для определённого класса
     */
    fun bind(resolver : ITypeResolver<*>, with : KClass<*>) : ResolverService {
        map[with] = resolver
        return this
    }

    /**
     * Возвращает объект [ITypeResolver]
     * @throws [NoSuchElementException]
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> getForType() : ITypeResolver<T> {
        val resolver = map[T::class]
        if (resolver != null)
            return resolver as ITypeResolver<T>
        throw NoSuchElementException("There is no one resolver for type ${T::class.qualifiedName}")
    }
}