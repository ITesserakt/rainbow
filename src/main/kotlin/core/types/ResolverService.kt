package core.types

object ResolverService {
    public val map = HashMap<Class<*>, ITypeResolver<*>>()

    inline fun <reified T> getForType() : ITypeResolver<T> {
        if(map[T::class.java] != null)
            return (map[T::class.java] as ITypeResolver<T>?)!!
        throw NoSuchElementException("Подходящий конвертер для типа ${T::class.java.name} не найден!")
    }

    inline fun <reified T> addResolver(resolver: ITypeResolver<T>) {
        map[T::class.java] = resolver
    }
}