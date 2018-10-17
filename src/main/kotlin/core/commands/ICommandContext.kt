package core.commands

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.*

interface ICommandContext {
    val channel : IChannel
    val message : IMessage
    val author : IUser
    val client : IDiscordClient
    val guild : IGuild
    val args : Array<String>
}
