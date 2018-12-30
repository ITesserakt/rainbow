package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.JoinContext
import sx.blah.discord.api.events.EventSubscriber
import sx.blah.discord.handle.impl.events.guild.member.UserJoinEvent
import sx.blah.discord.handle.obj.IChannel
import java.net.URL

@Suppress("FunctionName")
class JoinHandler : Handler() {
    @EventSubscriber
    fun OnUserJoined(event: UserJoinEvent) {
        val context = JoinContext(event)
        val file = URL("https://i.imgur.com/utPRe0e.gif")
        val channel: IChannel = context.guild.channels.firstOrNull { it.name.contains("bot", true) }
                ?: context.guild.defaultChannel

        channel.sendFile("Добро пожаловать, ${context.user.name}!", file.openStream(), file.file)
    }
}