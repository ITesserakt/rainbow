package core.types

import core.commands.ICommandContext

class BooleanResolver : ITypeResolver <Boolean>{
    override fun read(context: ICommandContext, input: String): Boolean {
        return input.toBoolean()
    }
}