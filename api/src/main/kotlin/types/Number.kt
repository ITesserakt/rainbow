package types

import context.ICommandContext
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty

abstract class NumberResolver <T : Number> : ITypeResolver<T> {
    override fun read(context: ICommandContext, input: String): Mono<T> =
        Mono.justOrEmpty(readWithNulls(input))
                .switchIfEmpty {
                    throw ClassCastException("Введенное значение не является числом!")
                }.map { it!! }

    abstract fun readWithNulls(input: String): T?
}

class IntResolver : NumberResolver<Int>() {
    override fun readWithNulls(input: String): Int? = input.toIntOrNull()
}

class LongResolver : NumberResolver<Long>() {
    override fun readWithNulls(input: String): Long? = input.toLongOrNull()
}

class FloatResolver : NumberResolver<Float>() {
    override fun readWithNulls(input: String): Float? = input.toFloatOrNull()
}

class ShortResolver : NumberResolver<Short>() {
    override fun readWithNulls(input: String): Short? = input.toShortOrNull()
}

class ByteResolver : NumberResolver<Byte>() {
    override fun readWithNulls(input: String): Byte? = input.toByteOrNull()
}

class DoubleResolver : NumberResolver<Double>() {
    override fun readWithNulls(input: String): Double? = input.toDoubleOrNull()
}