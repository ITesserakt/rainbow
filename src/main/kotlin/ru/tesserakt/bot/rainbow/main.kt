@file:JvmName("main")
package ru.tesserakt.bot.rainbow

import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.entity.User
import discord4j.core.event.domain.message.MessageCreateEvent
import ru.tesserakt.bot.rainbow.core.commands.GCommandProvider
import ru.tesserakt.bot.rainbow.core.handlers.GCommandHandler
import ru.tesserakt.bot.rainbow.core.types.*
import ru.tesserakt.bot.rainbow.modules.AdminsModule
import ru.tesserakt.bot.rainbow.modules.DeveloperModule
import ru.tesserakt.bot.rainbow.modules.HelpModule
import ru.tesserakt.bot.rainbow.util.on
import ru.tesserakt.bot.rainbow.util.token

fun main() {
    reactor.util.Loggers.useSl4jLoggers()

    val client = DiscordClientBuilder(token).build()
    client.eventDispatcher.on<MessageCreateEvent>()
            .doOnNext { GCommandHandler().handle(it) }
            .subscribe()

    GCommandProvider
            .addModule(DeveloperModule())
            .addModule(HelpModule())
            .addModule(AdminsModule())

    ResolverService
            .bind(StringResolver() to String::class)
            .bind(RoleResolver() to Role::class)
            .bind(IntResolver() to Int::class)
            .bind(LongResolver() to Long::class)
            .bind(FloatResolver() to Float::class)
            .bind(UserResolver() to User::class)

    client.login().block()
}