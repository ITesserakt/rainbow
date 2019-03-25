package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class LongResolverTest : ResolverTestTemplate<Long>() {
    override val resolver = LongResolver()

    @Test
    suspend fun `Valid string, parse to long, expect Mono(long)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "39"), 39L)
    }

    @Test
    fun `Invalid string, parse to long, expect exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                resolver.read(fakeContext, "a")
            }
        }
    }
}