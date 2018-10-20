package core

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

data class JoinContext (private val event : UserJoinEvent) : IContext {
    override val guild: IGuild = event.guild
    override val client: IDiscordClient = event.client
    override val user: IUser = event.user
    val joinTime = event.joinTime
}