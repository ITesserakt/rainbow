package core.types

import core.commands.CommandContext
import sx.blah.discord.handle.obj.IRole

class RoleResolver : ITypeResolver<IRole> {
    override fun read(context: CommandContext, input: String): IRole {
        val fromPureId : Long? = input.toLongOrNull()
        val guild = context.guild

        return if(input.startsWith('<') && input.endsWith('>')) {
            guild.getRoleByID(input.substring(3, input.length - 1).toLong())
        } else if (fromPureId != null) {
            guild.getRoleByID(fromPureId)
        } else {
            guild.getRolesByName(input).first()
        }
    }
}