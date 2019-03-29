package types

import context.ICommandContext

internal abstract class NumberResolver<T : Number> : ITypeResolver<T> {
    override suspend fun read(context: ICommandContext, input: String): T =
        readWithNulls(input) ?: throw NumberFormatException("`$input` не является числом")

    abstract fun readWithNulls(input: String): T?
}

internal class IntResolver : NumberResolver<Int>() {
    override fun readWithNulls(input: String): Int? = input.toIntOrNull()
}

internal class LongResolver : NumberResolver<Long>() {
    override fun readWithNulls(input: String): Long? = input.toLongOrNull()
}

internal class FloatResolver : NumberResolver<Float>() {
    override fun readWithNulls(input: String): Float? = input.toFloatOrNull()
}

internal class ShortResolver : NumberResolver<Short>() {
    override fun readWithNulls(input: String): Short? = input.toShortOrNull()
}

internal class ByteResolver : NumberResolver<Byte>() {
    override fun readWithNulls(input: String): Byte? = input.toByteOrNull()
}

internal class DoubleResolver : NumberResolver<Double>() {
    override fun readWithNulls(input: String): Double? = input.toDoubleOrNull()
}