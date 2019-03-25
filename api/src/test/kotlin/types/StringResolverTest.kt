package types

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class StringResolverTest : ResolverTestTemplate<String>() {
    override val resolver: ITypeResolver<String> = StringResolver()

    @Test
    suspend fun `string, parse to string, Mono(string)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "test"), "test")
    }
}