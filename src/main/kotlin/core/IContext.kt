package core

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

internal interface IContext {
    val user : IUser
    val client : IDiscordClient
    val guild : IGuild
}
