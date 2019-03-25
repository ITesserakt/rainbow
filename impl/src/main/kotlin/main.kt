import command.CommandLoader
import command.CommandRegistry
import command.GuildCommandProvider
import command.PrivateChannelCommandProvider
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.Role
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import handler.GuildCommandHandler
import handler.PrivateChannelCommandHandler
import handler.ReadyEventHandler
import reactor.util.Logger
import reactor.util.Loggers
import types.MemberResolver
import types.MessageChannelResolver
import types.RoleResolver
import types.resolverProvider
import util.on
import util.plusAssign
import java.time.LocalTime

private val logger: Logger = Loggers.getLogger(Class.forName("MainKt"))

fun main() {
    logger.info("Starting loading of bot...")

    val token = System.getenv("TOKEN")
    val client = DiscordClientBuilder(token).build()

    specifyEvents(client)

    resolverProvider {
        bind<MessageChannel>() with MessageChannelResolver()
        bind<Role>() with RoleResolver()
        bind<Member>() with MemberResolver()
    }

    CommandLoader("modules")
        .load(
            CommandRegistry(
                GuildCommandProvider, PrivateChannelCommandProvider
            )
        )

//    DynamicPresence(client, Duration.ofSeconds(5))
//        .start()
//        .subscribe()

    client.login().block()
}

private fun specifyEvents(client: DiscordClient) {
    with(client.eventDispatcher) {
        on<ReadyEvent>() += ReadyEventHandler()
        on<MessageCreateEvent>() += GuildCommandHandler()
        on<MessageCreateEvent>() += PrivateChannelCommandHandler()
    }
}

lateinit var startedTime: LocalTime