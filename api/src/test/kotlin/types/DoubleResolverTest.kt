package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class DoubleResolverTest : ResolverTestTemplate<Double>() {
    override val resolver = DoubleResolver()

    @Test
    fun `Valid string, parse to double, expect Mono(double)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "39"), 39.0)
    }

    @Test
    fun `Invalid string, parse to double, expect exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                resolver.read(fakeContext, "a")
            }
        }
    }
}