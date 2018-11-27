package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext

class FloatResolver : NumberResolver<Float>() {
    override fun read(context: ICommandContext, input: String): Float =
            super.read(context, input.toFloatOrNull())
}