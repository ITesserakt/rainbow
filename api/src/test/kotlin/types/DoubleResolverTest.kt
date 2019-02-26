package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class DoubleResolverTest : ResolverTestTemplate<Double>() {
    override val resolver = DoubleResolver()

    @Test
    fun `Valid string, parse to double, expect Mono(double)`() {
        StepVerifier.create(resolver.read(fakeContext, "39"))
                .expectNext(39.0)
                .verifyComplete()
    }

    @Test
    fun `Invalid string, parse to double, expect exception`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectError(ClassCastException::class)
                .verifyThenAssertThat()
                .hasOperatorErrorWithMessage("Введенное значение не является числом!")
    }
}