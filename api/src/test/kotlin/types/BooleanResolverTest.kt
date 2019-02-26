package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

internal class BooleanResolverTest : ResolverTestTemplate<Boolean>() {
    override val resolver: ITypeResolver<Boolean> = BooleanResolver()

    @Test
    fun `give true and expect true`() {
        StepVerifier.create(resolver.read(fakeContext, "True"))
                .expectNext(true)
                .verifyComplete()
    }

    @Test
    fun `give false and expect false`() {
        StepVerifier.create(resolver.read(fakeContext, "false"))
                .expectNext(false)
                .verifyComplete()
    }

    @Test
    fun `give something and expect false`() {
        StepVerifier.create(resolver.read(fakeContext, "rhg4hgh954hhth44thf"))
                .expectNext(false)
                .verifyComplete()
    }
}