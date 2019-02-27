import command.GuildCommandProvider
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
import handler.ReadyEventHandler
import modules.AdminModule
import modules.DeveloperModule
import modules.HelpModule
import modules.RainbowModule
import reactor.util.Logger
import reactor.util.Loggers
import types.MemberResolver
import types.MessageChannelResolver
import types.ResolverProvider
import types.RoleResolver
import util.Database
import util.DynamicPresence
import java.time.Duration
import java.time.LocalTime
import java.util.*

private val logger: Logger = Loggers.getLogger(Class.forName("MainKt"))

fun main() {
    logger.info("Starting loading of bot...")

    Database.forceCreateClient()

    val props = ResourceBundle.getBundle("config")
    val client = DiscordClientBuilder(props.getString("token")).build()

    specifyEvents(client)
    specifyModules()
    setupResolvers()

    DynamicPresence(client, Duration.ofSeconds(5))
            .start()
            .subscribe()

    client.login().block()
}

private fun specifyEvents(client: DiscordClient) {
    with(client.eventDispatcher) {
        on(ReadyEvent::class.java)
                .subscribe { ReadyEventHandler().handle(it) }
        on(MessageCreateEvent::class.java)
                .subscribe { GuildCommandHandler().handle(it) }
        on(MemberJoinEvent::class.java)
                .subscribe { JoinHandler().handle(it) }
    }
}

fun setupResolvers() {
    logger.info("Starting loading of resolvers...")

    ResolverProvider.bind(RoleResolver() to Role::class)
            .bind(MemberResolver() to Member::class)
            .bind(MessageChannelResolver() to MessageChannel::class)

    logger.info("Successfully loaded resolvers...")
}

private fun specifyModules() {
    logger.info("Starting loading of commands...")

    GuildCommandProvider
            .addModule(HelpModule())
            .addModule(AdminModule())
            .addModule(RainbowModule())
            .addModule(DeveloperModule())

    logger.info("Successfully loaded ${GuildCommandProvider.commands.size} commands")
}

lateinit var startedTime: LocalTime