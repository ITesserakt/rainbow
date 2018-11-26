package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext

class IntResolver : NumberResolver<Int>() {
    override fun read(context: ICommandContext, input: String): Int =
        super.read(context, input.toIntOrNull())
}

