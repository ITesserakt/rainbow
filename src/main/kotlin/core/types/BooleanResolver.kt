package core.types

import core.commands.CommandContext

internal class BooleanResolver : ITypeResolver <Boolean>{
    override fun read(context: CommandContext, input: String): Boolean {
        return input.toBoolean()
    }
}