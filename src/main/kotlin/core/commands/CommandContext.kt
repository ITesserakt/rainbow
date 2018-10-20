package core.commands

import core.IContext
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.util.*

data class CommandContext (private val event : MessageReceivedEvent, val args : Array<String>) : IContext {
    val channel = event.channel!!
    override val user = event.author!!
    val message = event.message!!
    override val client = event.client!!
    override val guild = event.guild!!

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandContext

        if (event != other.event) return false
        if (!Arrays.equals(args, other.args)) return false
        if (channel != other.channel) return false
        if (user != other.user) return false
        if (message != other.message) return false
        if (client != other.client) return false
        if (guild != other.guild) return false

        return true
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