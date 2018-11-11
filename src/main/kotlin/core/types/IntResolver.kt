package core.types

import core.ICommandContext

class IntResolver : NumberResolver<Int>() {
    override fun read(context: ICommandContext, input: String): Int =
        super.read(context, input.toIntOrNull())
}

abstract class NumberResolver<T> : ITypeResolver<T> where T : Number{
    protected open fun read(context: ICommandContext, action: T?): T{
        if(action == null)
            throw NullPointerException("Введенное значение не является числом")
        return action
    }
}
