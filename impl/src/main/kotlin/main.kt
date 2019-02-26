import command.GuildCommandProvider
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.MessageChannel
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
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
import reactor.core.Disposable
import reactor.core.publisher.toMono
import reactor.util.Logger
import reactor.util.Loggers
import reactor.util.function.component1
import reactor.util.function.component2
import types.MemberResolver
import types.MessageChannelResolver
import types.ResolverProvider
import types.RoleResolver
import util.Database
import util.zipWith
import java.time.Duration
import java.time.LocalTime
import java.util.*

private val logger: Logger = Loggers.getLogger(Class.forName("MainKt"))

fun main() {
    Loggers.useSl4jLoggers()
    logger.info("Starting loading of bot...")

    Database.forceCreateClient()

    val props = ResourceBundle.getBundle("config")
    val client = DiscordClientBuilder(props.getString("token")).build()

    specifyEvents(client)
    specifyModules()
    setupResolvers()
    enableDynamicPresence(client)

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

fun enableDynamicPresence(client: DiscordClient): Disposable = client.users.count()
        .zipWith(0)
        .mergeWith(client.guilds.count().zipWith(1))
        .mergeWith(client.regions.count().zipWith(2))
        .mergeWith(client.guilds.flatMap { it.channels }.count().zipWith(3))
        .mergeWith(Thread.activeCount().toLong().toMono().zipWith(4))
        .delayElements(Duration.ofSeconds(5))
        .map { (value, index) ->
            val words = arrayOf("users", "guilds", "regions", "channels", "threads")
            "$value ${words[index]}"
        }.doOnNext {
            client.updatePresence(Presence.online(Activity.watching("$it | say !help"))).subscribe()
        }.repeat()
        .subscribe()

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