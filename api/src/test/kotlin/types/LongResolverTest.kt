package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class LongResolverTest : ResolverTestTemplate<Long>() {
    override val resolver = LongResolver()

    @Test
    fun `Valid string, parse to long, expect Mono(long)`() {
        StepVerifier.create(resolver.read(fakeContext, "39"))
                .expectNext(39L)
                .verifyComplete()
    }

    @Test
    fun `Invalid string, parse to long, expect exception`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectError(ClassCastException::class)
                .verifyThenAssertThat()
                .hasOperatorErrorWithMessage("Введенное значение не является числом!")
    }
}