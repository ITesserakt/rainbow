package handler

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GuildCommandHandlerTest {
    @Test
    fun `quote splitting`() {
        fun resolve(s: String): List<String> =
            s.split(Regex("""\s+(?=([^"]*"[^"]*")*[^"]*$)"""))
                .map { it.replace("\"", "") }
                .filter { it.isNotEmpty() }

        val s = """!help "some fail" ""  and even this"""
        val s2 = """!rainbow_stop "rainbow stop" some"""
        val s3 = "!help"
        Assertions.assertEquals(resolve(s), listOf("!help", "some fail", "and", "even", "this"))
        Assertions.assertEquals(resolve(s2), listOf("!rainbow_stop", "rainbow stop", "some"))
        Assertions.assertEquals(resolve(s3), listOf("!help"))
    }
}