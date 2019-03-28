package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class FloatResolverTest : ResolverTestTemplate<Float>() {
    override val resolver = FloatResolver()

    @Test
    fun `Valid string, parse to float, expect Mono(float)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "39"), 39.0f)
    }

    @Test
    fun `Invalid string, parse to float, expect exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                resolver.read(fakeContext, "a")
            }
        }
    }
}