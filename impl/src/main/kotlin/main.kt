import bot.DynamicPresence
import command.CommandLoader
import command.CommandRegistry
import command.GuildCommandProvider
import command.PrivateChannelCommandProvider
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.Role
import discord4j.core.event.domain.guild.MemberJoinEvent
import discord4j.core.event.domain.lifecycle.ReadyEvent
import discord4j.core.event.domain.message.MessageCreateEvent
import handler.GuildCommandHandler
import handler.JoinHandler
import handler.PrivateChannelCommandHandler
import handler.ReadyEventHandler
import reactor.util.Logger
import reactor.util.Loggers
import types.MemberResolver
import types.MessageChannelResolver
import types.RoleResolver
import types.resolverProvider
import util.on
import java.time.Duration
import java.time.LocalTime
import java.util.*

private val logger: Logger = Loggers.getLogger(Class.forName("MainKt"))

fun main() {
    logger.info("Starting loading of bot...")

    val props = ResourceBundle.getBundle("config")
    val client = DiscordClientBuilder(props.getString("token")).build()

    specifyEvents(client)

    resolverProvider {
        bind<MessageChannel>() with MessageChannelResolver()
        bind<Role>() with RoleResolver()
        bind<Member>() with MemberResolver()
    }

    CommandRegistry.addProvider(GuildCommandProvider)
            .addProvider(PrivateChannelCommandProvider)

    CommandLoader("modules")
            .load()

    DynamicPresence(client, Duration.ofSeconds(5))
            .start()
            .subscribe()

    client.login().block()
}

private fun specifyEvents(client: DiscordClient) {
    with(client.eventDispatcher) {
        on<ReadyEvent>()
                .subscribe { ReadyEventHandler().handle(it) }
        on<MessageCreateEvent>()
                .subscribe {
                    GuildCommandHandler().handle(it)
                    PrivateChannelCommandHandler().handle(it)
                }
        on<MemberJoinEvent>()
                .subscribe { JoinHandler().handle(it) }
    }
}

lateinit var startedTime: LocalTime

//TODO доделать memberJoinEvent
//TODO доделать нормальный мут
//TODO добавить аннотации: group, requireDeveloper, requireOwner