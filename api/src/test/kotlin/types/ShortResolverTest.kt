package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class ShortResolverTest : ResolverTestTemplate<Short>() {
    override val resolver = ShortResolver()

    @Test
    fun `Valid string, parse to short, expect Mono(short)`() {
        StepVerifier.create(resolver.read(fakeContext, "39"))
                .expectNext(39)
                .verifyComplete()
    }

    @Test
    fun `Invalid string, parse to short, expect exception`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectError(ClassCastException::class)
                .verifyThenAssertThat()
                .hasOperatorErrorWithMessage("Введенное значение не является числом!")
    }
}