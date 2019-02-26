package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class StringResolverTest : ResolverTestTemplate<String>() {
    override val resolver: ITypeResolver<String> = StringResolver()

    @Test
    fun `string, parse to string, Mono(string)`() {
        StepVerifier.create(resolver.read(fakeContext, "test"))
                .expectNext("test")
                .verifyComplete()
    }
}