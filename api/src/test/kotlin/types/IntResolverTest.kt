package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class IntResolverTest : ResolverTestTemplate<Int>() {
    override val resolver = IntResolver()

    @Test
    fun `Valid string, parse to int, expect Mono(int)`() {
        StepVerifier.create(resolver.read(fakeContext, "39"))
                .expectNext(39)
                .verifyComplete()
    }

    @Test
    fun `Invalid string, parse to int, expect exception`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectError(ClassCastException::class)
                .verifyThenAssertThat()
                .hasOperatorErrorWithMessage("Введенное значение не является числом!")
    }
}