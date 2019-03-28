package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class ByteResolverTest : ResolverTestTemplate<Byte>(){
    override val resolver = ByteResolver()

    @Test
    fun `Valid string, parse to byte, expect Mono(byte)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "2"), 2)
    }

    @Test
    fun `Invalid string, parse to byte, expect exception`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                resolver.read(fakeContext, "a")
            }
        }
    }
}