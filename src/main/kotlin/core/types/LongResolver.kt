package core.types

import core.commands.CommandContext

internal class LongResolver : ITypeResolver<Long> {
    override fun read(context: CommandContext, input: String): Long {
        val guild = context.guild

        return if (input.startsWith('<') && input.endsWith('>'))
            input.substring(2, input.length - 1).toLong()
        else if (input.toLongOrNull() != null)
            input.toLong()
        else
            guild.getUsersByName(input).first().longID
    }
}