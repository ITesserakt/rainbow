
import bot.DynamicPresence
import command.CommandLoader
import command.CommandRegistry
import command.GuildCommandProvider
import command.PrivateChannelCommandProvider
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.presence.Presence
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import handler.GuildCommandHandler
import handler.PrivateChannelCommandHandler
import handler.ReadyEventHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.runBlocking
import reactor.util.Logger
import reactor.util.Loggers
import types.MemberResolver
import types.MessageChannelResolver
import types.ResolverProvider
import types.RoleResolver
import util.awaitOrNull
import util.discordClientBuilder
import util.on
import util.plusAssign
import java.time.Duration
import java.time.LocalTime

private val logger: Logger = Loggers.getLogger(Class.forName("MainKt"))

@ExperimentalCoroutinesApi
@ObsoleteCoroutinesApi
fun main() = runBlocking<Unit> {
    logger.info("Starting loading of bot...")

    val token = System.getenv("TOKEN")
    val client = discordClientBuilder(token) {
        setInitialPresence(Presence.doNotDisturb())
    }.build()

    with(client.eventDispatcher) {
        on<ReadyEvent>() += ReadyEventHandler()
        on<MessageCreateEvent>() += GuildCommandHandler()
        on<MessageCreateEvent>() += PrivateChannelCommandHandler()
    }

    with(ResolverProvider) {
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

    DynamicPresence(client, Duration.ofSeconds(5)).start()

    client.login().awaitOrNull()
}

lateinit var startedTime: LocalTime