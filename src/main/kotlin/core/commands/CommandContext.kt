package core.commands

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent
import java.util.*

data class CommandContext (private val event : MessageReceivedEvent, override val args : Array<String>) : ICommandContext {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandContext

        if (event != other.event) return false
        if (!Arrays.equals(args, other.args)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = event.hashCode()
        result = 31 * result + Arrays.hashCode(args)
        return result
    }

    override val channel = event.channel!!
    override val author = event.author!!
    override val message = event.message!!
    override val client = event.client!!
    override val guild = event.guild!!
}