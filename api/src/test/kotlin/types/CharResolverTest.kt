package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class CharResolverTest : ResolverTestTemplate<Char>() {
    override val resolver: ITypeResolver<Char> = CharResolver()

    @Test
    fun `one letter, parse to char, Mono(char)`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectNext('a')
                .verifyComplete()
    }

    @Test
    fun `string, parse to char, Mono(char)`() {
        StepVerifier.create(resolver.read(fakeContext, "abc"))
                .expectNext('a')
                .verifyComplete()
    }
}