@file:JvmName("main")
package ru.tesserakt.bot.rainbow

import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.util.Loggers
import ru.tesserakt.bot.rainbow.core.commands.GCommandProvider
import ru.tesserakt.bot.rainbow.core.handlers.GCommandHandler
import ru.tesserakt.bot.rainbow.core.types.*
import ru.tesserakt.bot.rainbow.modules.AdminsModule
import ru.tesserakt.bot.rainbow.modules.DeveloperModule
import ru.tesserakt.bot.rainbow.modules.HelpModule
import ru.tesserakt.bot.rainbow.util.on
import ru.tesserakt.bot.rainbow.util.token
import java.time.Duration
import java.time.Instant
import kotlin.concurrent.timer

private var loginTime : Instant? = null
private val logger = Loggers.getLogger("main")

fun main() {
    reactor.util.Loggers.useSl4jLoggers()

    val client = DiscordClientBuilder(token).build()

    setupEvents(client)
    setupModules()
    setupResolvers()
    setupDynamicPresence(client)

    client.login().block()
}

private fun setupEvents(client: DiscordClient) {
    client.eventDispatcher.on<ReadyEvent>()
            .subscribe {
                logger.info("Successfully connected")
                logger.info("Observing on ${it.guilds.size} guilds")
                loginTime = Instant.now()
            }

    client.eventDispatcher.on<MessageCreateEvent>()
            .doOnNext { GCommandHandler().handle(it) }
            .subscribe()
}

fun setupDynamicPresence(client : DiscordClient) {
    var index = 0
    timer("Presence", true, 0L, 5000) {
        val state = when (index) {
            0 -> "${client.guilds.count().block()} guilds"
            1 -> "${client.users.count().block()} users"
            2 -> "${client.regions.count().block()} regions"
            3 -> "${client.guilds.flatMap { it.channels }.count().block()} channels"
            4 -> "Uptime: ${uptime?.toHours()}"
            5 -> "Ping: ${client.responseTime} ms"
            else -> ""
        }

        client.updatePresence(Presence.online(Activity.watching("$state | say !help")))
                .subscribe()

        index++
        if (index >= 6) index = 0
    }
}

private fun setupModules() {
    GCommandProvider
            .addModule(DeveloperModule())
            .addModule(HelpModule())
            .addModule(AdminsModule())
}

private fun setupResolvers() {
    ResolverService
            .bind(StringResolver() to String::class)
            .bind(RoleResolver() to Role::class)
            .bind(IntResolver() to Int::class)
            .bind(LongResolver() to Long::class)
            .bind(FloatResolver() to Float::class)
            .bind(UserResolver() to User::class)
}

val uptime : Duration?
    get() {
        if (loginTime != null) {
            return Duration.between(Instant.now(), loginTime)
        }
        return null
    }