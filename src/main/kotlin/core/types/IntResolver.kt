package core.types

import core.commands.ICommandContext

class IntResolver : ITypeResolver<Int> {
    override fun read(context: ICommandContext, input: String): Int {
        if (input.isNotEmpty())
            return input.toInt()
        throw EmptyInput()
    }
}

class EmptyInput : Exception("Входная строка пуста")
