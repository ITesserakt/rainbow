package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext

abstract class NumberResolver<T> : ITypeResolver<T> where T : Number{
    protected open fun read(context: ICommandContext, action: T?): T{
        if(action == null)
            throw NullPointerException("Введенное значение не является числом")
        return action
    }
}