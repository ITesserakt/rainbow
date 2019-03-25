package types

import context.ICommandContext
import util.toOptional

abstract class NumberResolver<T : Number> : ITypeResolver<T> {
    override suspend fun read(context: ICommandContext, input: String): T =
        readWithNulls(input).toOptional()
            .orElseThrow { IllegalArgumentException("Введенное значение не является числом!") }

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