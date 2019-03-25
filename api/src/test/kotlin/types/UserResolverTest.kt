package types

import discord4j.core.`object`.entity.User
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import util.toSnowflake

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserResolverTest : ResolverTestTemplate<User>() {
    override val resolver: ITypeResolver<User> = UserResolver()
    private val id = 316249690092077065L.toSnowflake()

    @Test
    suspend fun `mention, parse to user entity, Mono(user)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "<&316249690092077065>"), id)
    }

    @Test
    suspend fun `non-existent mention, parse to user entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "<&hrthdb ggfht>")
            }
        }
    }

    @Test
    suspend fun `id, parse to user entity, Mono(user)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "316249690092077065"), id)
    }

    @Test
    fun `non-existent id, parse to user entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "2872875827278272")
            }
        }
    }

    //Не проходит, потому что нельзя заблочить client.login() и client.users пустой
    @Disabled
    @Test
    suspend fun `name with discriminator, parse to user entity, Mono(user)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "VoV4ik#5413").id, id)
    }

    @Test
    fun `something terrible, parse to user entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "ergvtgtyrjtjyehabytwj")
            }
        }
    }
}