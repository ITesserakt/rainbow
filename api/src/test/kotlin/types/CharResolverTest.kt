package types

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CharResolverTest : ResolverTestTemplate<Char>() {
    override val resolver: ITypeResolver<Char> = CharResolver()

    @Test
    suspend fun `one letter, parse to char, Mono(char)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "a"), 'a')
    }

    @Test
    suspend fun `string, parse to char, Mono(char)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "abs"), 'a')
    }
}