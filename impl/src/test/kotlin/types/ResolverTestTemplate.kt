package types

import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.domain.message.MessageCreateEvent
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ResolverTestTemplate <T> : Assertions() {
    protected lateinit var fakeContext : ICommandContext
    protected abstract val resolver : ITypeResolver<T>
    private lateinit var client: DiscordClient

    @BeforeAll
    protected open fun setUp() {
        val token = ResourceBundle.getBundle("config").getString("token")

        client = DiscordClientBuilder(token).build()

        val fakeGuildId = Snowflake.of(490951935894093850)
        val fakeEvent = MessageCreateEvent(
                client,
                mockk(),
                fakeGuildId.asLong(),
                client.self.flatMap { it.asMember(fakeGuildId) }.block()
        )
        fakeContext = GuildCommandContext(fakeEvent, emptyList())
        client.login().subscribe()
    }
}