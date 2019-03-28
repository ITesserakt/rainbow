package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class IntResolverTest : ResolverTestTemplate<Int>() {
    override val resolver = IntResolver()

    @Test
    fun `Valid string, parse to int, expect Mono(int)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "39"), 39)
    }

    @Test
    fun `Invalid string, parse to int, expect exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                resolver.read(fakeContext, "a")
            }
        }
    }
}