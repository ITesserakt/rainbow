package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext

internal class LongResolver : NumberResolver<Long>() {
    override fun read(context: ICommandContext, input: String): Long =
            super.read(context, input.toLongOrNull())
}