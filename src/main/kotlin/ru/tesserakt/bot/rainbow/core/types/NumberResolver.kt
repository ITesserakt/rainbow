package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.context.ICommandContext


abstract class NumberResolver <T : Number>: ITypeResolver<T> {
    fun read(cast : T?): T {
        return cast ?: throw IllegalArgumentException("Введенное значение не является числом!")
    }
}

class IntResolver : NumberResolver<Int>() {
    override fun read(context: ICommandContext, input: String): Int = read(input.toIntOrNull())
}

class FloatResolver : NumberResolver<Float>() {
    override fun read(context: ICommandContext, input: String): Float = read(input.toFloatOrNull())
}

class LongResolver : NumberResolver<Long>() {
    override fun read(context: ICommandContext, input: String): Long = read(input.toLongOrNull())
}