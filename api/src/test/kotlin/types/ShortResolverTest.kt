package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ShortResolverTest : ResolverTestTemplate<Short>() {
    override val resolver = ShortResolver()

    @Test
    fun `Valid string, parse to short, expect Mono(short)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "39"), 39)
    }

    @Test
    fun `Invalid string, parse to short, expect exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                resolver.read(fakeContext, "a")
            }
        }
    }
}