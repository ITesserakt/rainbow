package handler

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class GuildCommandHandlerTest {
    @Test
    fun `quote splitting`() {
        fun resolveContent(content: String): List<String> {
            var result = ""
            var isInQuote = false
            for (i in content) {
                if (i == '"') {
                    isInQuote = !isInQuote
                    continue
                }
                if (i == ' ') {
                    result += if (isInQuote) '_'
                    else ' '
                    continue
                }
                result += i
            }
            return result
                .split(' ')
                .filter { it.isNotEmpty() }
                .map { it.replace('_', ' ') }
        }

        val s = """!help "some fail" ""  and even this"""
        Assertions.assertEquals(resolveContent(s), listOf("!help", "some fail", "and", "even", "this"))
    }
}