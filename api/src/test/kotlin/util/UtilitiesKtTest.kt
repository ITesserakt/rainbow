package util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.random.Random

internal class UtilitiesKtTest {
    @Test
    fun `optional extension`() {
        assertEquals(true.toOptional(), Optional.of(true))
    }

    @Test
    fun `multiply string by number`() {
        assertEquals("-=" * 3, "-=-=-=")
    }

    @RepeatedTest(100)
    fun `power of 2 number`() {
        val first = Random.nextLong(100000)
        val second = Random.nextInt(999)
        val result = Math.pow(first.toDouble(), second.toDouble())
        assertEquals(first `**` second, result)
        println("$first ** $second = $result")
    }
}