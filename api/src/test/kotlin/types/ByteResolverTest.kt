package types

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class ByteResolverTest : ResolverTestTemplate<Byte>(){
    override val resolver = ByteResolver()

    @Test
    fun `Valid string, parse to byte, expect Mono(byte)`() {
        StepVerifier.create(resolver.read(fakeContext, "39"))
                .expectNext(39)
                .verifyComplete()
    }

    @Test
    fun `Invalid string, parse to byte, expect exception`() {
        StepVerifier.create(resolver.read(fakeContext, "a"))
                .expectError(ClassCastException::class)
                .verifyThenAssertThat()
                .hasOperatorErrorWithMessage("Введенное значение не является числом!")
    }
}