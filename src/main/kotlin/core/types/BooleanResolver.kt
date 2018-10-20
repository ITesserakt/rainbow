package core.types

import core.commands.CommandContext

class BooleanResolver : ITypeResolver <Boolean>{
    override fun read(context: CommandContext, input: String): Boolean {
        return input.toBoolean()
    }
}