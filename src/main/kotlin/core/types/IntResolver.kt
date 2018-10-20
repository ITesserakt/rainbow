package core.types

import core.commands.CommandContext

class IntResolver : ITypeResolver<Int> {
    override fun read(context: CommandContext, input: String): Int {
        if (input.isNotEmpty())
            return input.toInt()
        throw EmptyInput()
    }
}

class EmptyInput : Exception("Входная строка пуста")
