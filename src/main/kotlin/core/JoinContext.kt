package core

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser
import java.time.Instant

/**
 * Контекст события UserJoinEvent
 */
data class JoinContext (private val event : UserJoinEvent) : IContext {
    override var guild: IGuild = event.guild
    override val client: IDiscordClient = event.client
    override var user: IUser = event.user
    val joinTime: Instant = event.joinTime
}