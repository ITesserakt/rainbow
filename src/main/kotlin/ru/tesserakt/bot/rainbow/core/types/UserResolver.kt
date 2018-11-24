package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext
import sx.blah.discord.handle.obj.IUser

internal class UserResolver : ITypeResolver<IUser> {
    override fun read(context: ICommandContext, input: String): IUser {
        val fromPureId : Long? = input.toLongOrNull()
        val guild = context.guild

        return if (input.startsWith('<') && input.endsWith('>')) {
            guild.getUserByID(input.substring(2, input.length - 1).toLong())
        } else if (fromPureId != null) {
            guild.getUserByID(fromPureId)
        } else {
            guild.getUsersByName(input).first()
        }
    }
}