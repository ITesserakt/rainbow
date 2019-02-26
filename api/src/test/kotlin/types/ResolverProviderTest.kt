package types

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

internal class ResolverProviderTest {

    @RepeatedTest(100)
    fun `get resolver by type from map and expect its`() {
        val resolver : ITypeResolver<String> by ResolverProvider
        assertEquals(resolver.javaClass, StringResolver().javaClass)
    }

    @Test
    fun `get non-exists resolver by type and expect error`() {
        assertThrows(NoSuchElementException::class.java) {
            ResolverProvider.get<Unit>()
        }
    }
}