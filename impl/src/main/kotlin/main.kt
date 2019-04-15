
import bot.DynamicPresence
import command.*
import discord4j.core.`object`.presence.Presence
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import handler.GuildCommandHandler
import handler.PrivateChannelCommandHandler
import handler.ReadyEventHandler
import kotlinx.coroutines.runBlocking
import reactor.util.Logger
import reactor.util.Loggers
import util.DiscordClientBuilder
import util.on
import util.plusAssign
import java.time.Duration

private val logger: Logger = Loggers.getLogger(Class.forName("MainKt"))

fun main() = runBlocking {
    logger.info("Starting loading of bot...")

    val client = DiscordClientBuilder {
        token = System.getenv("TOKEN")
        initialPresence = Presence.doNotDisturb()
        commandLoadersPackage = "modules"
    }

    with(client.eventDispatcher) {
        on<ReadyEvent>() += ReadyEventHandler()
        on<MessageCreateEvent>() += GuildCommandHandler()
        on<MessageCreateEvent>() += PrivateChannelCommandHandler()
    }

    client.commandLoader.load(
        CommandRegistry(GuildCommandProvider, PrivateChannelCommandProvider)
    )

    DynamicPresence(client, Duration.ofSeconds(5)).start()

    client.loginAsync()
}