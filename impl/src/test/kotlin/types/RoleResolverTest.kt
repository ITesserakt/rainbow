package types

import discord4j.core.`object`.entity.Role
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.toSnowflake

internal class RoleResolverTest : ResolverTestTemplate<Role>() {
    override val resolver: ITypeResolver<Role> = RoleResolver()
    private val id = 510459822819246091.toSnowflake()

    @Test
    fun `non-existing mention but regex is true`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "7354987450949518904390")
            }
        }
    }

    @Test
    fun `normal id, parse to role entity and expect Mono(role)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "510459822819246091").id, id)
    }

    @Test
    fun `role name, parse to role entity and expect Mono(role)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "Admins").id, id)
    }

    @Test
    fun `invalid name, parse to role entity and expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "7354987450949518904390")
            }
        }
    }
}