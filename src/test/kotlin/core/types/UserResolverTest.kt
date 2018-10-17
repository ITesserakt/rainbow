package core.types

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UserResolverTest {

    @Test
    fun `tests right resolve of raw-user id` () {
        val testInput = "<@481169011262881793>"
        val result = testInput.substring(2, testInput.length - 1).toLongOrNull()
        assertEquals(481169011262881793, result)
    }
}