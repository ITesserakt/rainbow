package types

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class StringResolverTest : ResolverTestTemplate<String>() {
    override val resolver: ITypeResolver<String> = StringResolver()

    @Test
    fun `string, parse to string, Mono(string)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "test"), "test")
    }
}