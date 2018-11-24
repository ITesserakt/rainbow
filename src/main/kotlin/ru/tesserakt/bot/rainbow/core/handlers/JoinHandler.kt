package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.JoinContext
import ru.tesserakt.bot.rainbow.core.RESOURCES
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import java.io.File

@Suppress("FunctionName")
class JoinHandler : Handler() {
    @EventSubscriber
    fun OnUserJoined(event: UserJoinEvent) {
        val context = JoinContext(event)
        val file = File("$RESOURCES/welcome.gif")
        context.guild.defaultChannel.sendFile("Добро пожаловать, ${context.user.name}!", file)
    }
}