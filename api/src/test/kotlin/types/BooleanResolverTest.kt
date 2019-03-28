package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BooleanResolverTest : ResolverTestTemplate<Boolean>() {
    override val resolver: ITypeResolver<Boolean> = BooleanResolver()

    @Test
    fun `give true and expect true`() = runBlocking {
        Assertions.assertTrue(resolver.read(fakeContext, "true"))
    }

    @Test
    fun `give false and expect false`() = runBlocking {
        Assertions.assertFalse(resolver.read(fakeContext, "false"))
    }

    @Test
    fun `give something and expect false`() = runBlocking {
        Assertions.assertFalse(resolver.read(fakeContext, "rhg4hgh954hhth44thf"))
    }
}