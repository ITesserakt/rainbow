package handler

import context.ICommandContext
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.*
import org.junit.jupiter.api.extension.ExtendWith
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class ParserTest {
    @MockK
    private lateinit var fakeContext : ICommandContext
    private lateinit var client : DiscordClient
    private lateinit var parser : Parser
    private val id = Snowflake.of(481169011262881793L)

    @BeforeAll
    private fun setUp() {
        client = DiscordClientBuilder("NDgxMTY1MDA0NjEyMTczODI0.D0i6DQ.fSvsuP4feJNVeSvmSbyBs8t6bis").build()
        client.login().subscribe()

        every { fakeContext.client } returns client
        every { fakeContext.commandArgs } returns arrayOf("<&481169011262881793>")

        parser = Parser(fakeContext)
    }

    @AfterAll
    private fun tearDown() {
        client.logout().subscribe()
    }

    @Test
    fun `parse non-optional user id to user entity`() {
        StepVerifier.create(parser.parse(0, User::class, false))
                .assertNext { Assertions.assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `parse optional user id to user entity`() {
        StepVerifier.create(parser.parseOptional(0, User::class, false))
                .assertNext { Assertions.assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `parse nothing to empty mono`() {
        StepVerifier.create(parser.parseOptional(1, User::class, false))
                .expectNextCount(0)
                .verifyComplete()
    }

    @Test
    fun `parse non-existing argument, expect error`() {
        StepVerifier.create(parser.parse(1, User::class, false))
                .expectError(IllegalArgumentException::class.java)
                .verify()
    }
}