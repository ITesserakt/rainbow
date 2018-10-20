package core.types

import core.commands.CommandContext

class CharResolver : ITypeResolver<Char> {
    override fun read(context: CommandContext, input: String): Char {
        if (input.length == 1)
            return input[0]
        throw StringIndexOutOfBoundsException("Входные данные должны состоять из одного символа")
    }
}