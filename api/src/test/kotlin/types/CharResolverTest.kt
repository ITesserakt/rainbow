package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class CharResolverTest : ResolverTestTemplate<Char>() {
    override val resolver: ITypeResolver<Char> = CharResolver()

    @Test
    fun `one letter, parse to char, Mono(char)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "a"), 'a')
    }

    @Test
    fun `string, parse to char, Mono(char)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "abs"), 'a')
    }
}