package types

import context.ICommandContext
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import reactor.core.publisher.Mono
import java.util.*

@ExtendWith(MockKExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
abstract class ResolverTestTemplate <T> {
    @MockK
    protected lateinit var fakeContext : ICommandContext
    protected abstract val resolver : ITypeResolver<T>
    private var client: DiscordClient? = null

    @BeforeAll
    protected open fun setUp() {
        val token = ResourceBundle.getBundle("config").getString("token")

        client = DiscordClientBuilder(token).build()

        every { fakeContext.commandArgs } returns arrayOf()
        every { fakeContext.client } returns client!!
        every { fakeContext.member } returns Mono.empty()
        every { fakeContext.message } returns mockk()

        client?.login()?.subscribe()
    }
}