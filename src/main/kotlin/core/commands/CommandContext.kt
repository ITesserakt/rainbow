package core.commands

import core.IContext
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import sx.blah.discord.handle.obj.IChannel
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IMessage
import sx.blah.discord.handle.obj.IUser
import java.util.*

data class CommandContext (private val event : MessageReceivedEvent, val args : Array<String>) : IContext {
    val channel: IChannel = event.channel
    override val user: IUser = event.author
    val message: IMessage = event.message
    override val client: IDiscordClient = event.client
    override val guild: IGuild = event.guild

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        return compareProperties(other as? CommandContext)
    }

    private fun compareProperties(other: CommandContext?): Boolean {
        return when {
            event != other?.event -> false
            !Arrays.equals(args, other.args) -> false
            channel != other.channel -> false
            user != other.user -> false
            message != other.message -> false
            client != other.client -> false
            guild != other.guild -> false
            else -> true
        }
    }

    override fun hashCode(): Int {
        var result = event.hashCode()
        result = 31 * result + Arrays.hashCode(args)
        result = 31 * result + channel.hashCode()
        result = 31 * result + user.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + client.hashCode()
        result = 31 * result + guild.hashCode()
        return result
    }
}