package core.types

import core.ICommandContext

class CharResolver : ITypeResolver<Char> {
    override fun read(context: ICommandContext, input: String): Char {
        if (input.length == 1)
            return input[0]
        throw StringIndexOutOfBoundsException("Входные данные должны состоять из одного символа")
    }
}