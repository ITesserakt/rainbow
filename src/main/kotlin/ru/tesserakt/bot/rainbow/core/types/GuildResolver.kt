package ru.tesserakt.bot.rainbow.core.types

import ru.tesserakt.bot.rainbow.core.ICommandContext
import sx.blah.discord.handle.obj.IGuild

class GuildResolver : ITypeResolver<IGuild> {
    override fun read(context: ICommandContext, input: String): IGuild {
        val fromPureId = input.toLongOrNull()
        val client = context.client

        return if (input.startsWith('<') && input.endsWith('>')) {
            client.getGuildByID(input.substring(2, input.length - 1).toLong())
        } else if (fromPureId != null) {
            client.getGuildByID(fromPureId)
        } else {
            client.guilds.first { it.name == input}
        }
    }
}