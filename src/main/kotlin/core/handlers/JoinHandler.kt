package core.handlers

import core.JoinContext
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import java.io.File

@Suppress("FunctionName")
class JoinHandler {
    @EventSubscriber
    fun OnUserJoined(event: UserJoinEvent) {
        val context = JoinContext(event)
        val file = File("${File("").absolutePath}/src/main/resources/welcome.gif")
        context.guild.defaultChannel.sendFile("Добро пожаловать, ${context.user.name}!", file)
    }
}