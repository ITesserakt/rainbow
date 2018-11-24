package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext
import sx.blah.discord.handle.obj.IRole

class RoleResolver : ITypeResolver<IRole> {
    override fun read(context: ICommandContext, input: String): IRole {
        val fromPureId : Long? = input.toLongOrNull()
        val guild = context.guild

        return if (input.startsWith('<') && input.endsWith('>')) {
            guild.getRoleByID(input.substring(3, input.length - 1).toLong())
        } else if (fromPureId != null) {
            guild.getRoleByID(fromPureId)
        } else {
            guild.getRolesByName(input).first()
        }
    }
}