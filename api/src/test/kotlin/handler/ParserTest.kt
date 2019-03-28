package handler

import context.ICommandContext
import discord4j.core.DiscordClient
import discord4j.core.DiscordClientBuilder
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
internal class ParserTest {
    @MockK
    private lateinit var fakeContext: ICommandContext
    private lateinit var client: DiscordClient
    private lateinit var parser: Parser
    private val id = Snowflake.of(481169011262881793L)

    @BeforeAll
    private fun setUp() {
        val bundle = ResourceBundle.getBundle("config")
        client = DiscordClientBuilder(bundle.getString("token")).build()
        client.login().subscribe()

        every { fakeContext.client } returns client
        every { fakeContext.commandArgs } returns arrayOf("<&481169011262881793>")

        parser = Parser(fakeContext)
    }

    @Test
    fun `parse non-optional user id to user entity`() = runBlocking {
        val result = parser.parse(0, User::class, false)
        Assertions.assertEquals(result.id, id)
    }

    @Test
    fun `parse optional user id to user entity`() = runBlocking {
        val result = parser.parseOptional(0, User::class, false)
        Assertions.assertNotNull(result)
        Assertions.assertEquals(result!!.id, id)
    }

    @Test
    suspend fun `parse nothing to empty mono`() {
        val result = parser.parseOptional(1, User::class, false)
        Assertions.assertNull(result)
    }

    @Test
    fun `parse non-existing argument, expect error`() {
        Assertions.assertThrows(NoSuchElementException::class.java) {
            runBlocking {
                parser.parse(1, User::class, false)
            }
        }
    }
}