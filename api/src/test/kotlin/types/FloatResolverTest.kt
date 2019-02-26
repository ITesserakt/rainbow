package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class FloatResolverTest : ResolverTestTemplate<Float>() {
    override val resolver = FloatResolver()

    @Test
    fun `Valid string, parse to float, expect Mono(float)`() {
        StepVerifier.create(resolver.read(fakeContext, "39"))
                .expectNext(39F)
                .verifyComplete()
    }

    @Test
    fun `Invalid string, parse to float, expect exception`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectError(ClassCastException::class)
                .verifyThenAssertThat()
                .hasOperatorErrorWithMessage("Введенное значение не является числом!")
    }
}