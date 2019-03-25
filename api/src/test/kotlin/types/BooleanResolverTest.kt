package types

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class BooleanResolverTest : ResolverTestTemplate<Boolean>() {
    override val resolver: ITypeResolver<Boolean> = BooleanResolver()

    @Test
    suspend fun `give true and expect true`() {
        Assertions.assertTrue(resolver.read(fakeContext, "true"))
    }

    @Test
    suspend fun `give false and expect false`() {
        Assertions.assertFalse(resolver.read(fakeContext, "false"))
    }

    @Test
    suspend fun `give something and expect false`() {
        Assertions.assertFalse(resolver.read(fakeContext, "rhg4hgh954hhth44thf"))
    }
}