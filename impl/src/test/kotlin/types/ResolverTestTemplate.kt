package types

import context.GuildCommandContext
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.TextChannel
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.cast
import util.await
import util.toSnowflake
import java.util.*

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ResolverTestTemplate<T> : Assertions() {
    @MockK
    protected lateinit var fakeContext: GuildCommandContext
    protected abstract val resolver: ITypeResolver<T>
    private lateinit var client: DiscordClient

    @BeforeAll
    protected open fun setUp() {
        val token = ResourceBundle.getBundle("config").getString("token")

        client = DiscordClientBuilder(token).build()

        GlobalScope.launch {
            every { fakeContext.client } returns client
            every { fakeContext.guild } returns GlobalScope.async {
                client.getGuildById(490951935894093850.toSnowflake()).await()
            }
            every { fakeContext.guildId } returns 490951935894093850.toSnowflake()
            every { fakeContext.message } returns mockk()
            every { fakeContext.author } returns client.getUserById(316249690092077065.toSnowflake()).await()
            every { fakeContext.channel } returns GlobalScope.async {
                client.getChannelById(490951935894093858.toSnowflake())
                    .cast<TextChannel>()
                    .await()
            }
            every { fakeContext.commandArgs } returns arrayOf()
        }

        client.login().subscribe()
    }
}