package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.JoinContext
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent

@Suppress("FunctionName")
class JoinHandler : Handler() {
    @EventSubscriber
    fun OnUserJoined(event: UserJoinEvent) {
        val context = JoinContext(event)
        val file = Any::class.java.getResource("welcome.gif")
        context.guild.defaultChannel.sendFile("Добро пожаловать, ${context.user.name}!", file.openStream(), file.file)
    }
}