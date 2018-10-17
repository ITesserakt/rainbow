package core

import core.commands.CommandBuilder
import org.junit.jupiter.api.Test
import java.io.File

internal class JSONUtilsKtTest {
    private val path = "src/test/resources/write_test.json"

    @Test
    fun `JSON by simple types`() {
        val obj = 150686
        writeToJSON(path, obj)
        val expected = getParsedObject<Int>(path)
        assert(expected == obj)
    }

    @Test
    fun `JSON by complex types`() {
        val obj2 = CommandBuilder("test") {}.addSummary("It`s a test!").addAliases("Test1", "Test2")
        writeToJSON(path, obj2)
        val expected = getParsedObject<CommandBuilder>(path)
        println(expected)
        println(obj2)
    }
}