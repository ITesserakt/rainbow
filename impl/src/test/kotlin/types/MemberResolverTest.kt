package types

import discord4j.core.`object`.entity.Member
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.toSnowflake

class MemberResolverTest : ResolverTestTemplate<Member>() {
    override val resolver: ITypeResolver<Member> = MemberResolver()
    private val id = 316249690092077065L.toSnowflake()

    @Test
    suspend fun `mention, parse to member entity, Mono(member)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "<&316249690092077065>").id, id)
    }

    @Test
    fun `non-existent mention, parse to member entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "<&2549456125498458484>")
            }
        }
    }

    @Test
    suspend fun `id, parse to member entity, Mono(member)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "316249690092077065").id, id)
    }

    @Test
    fun `non-existent id, parse to member entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "2549456125498458484")
            }
        }
    }

    @Test
    suspend fun `name, parse to member entity, Mono(member)`() {
        Assertions.assertEquals(resolver.read(fakeContext, "VoV4ik").id, id)
    }

    @Test
    fun `something terrible, parse to member entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "some")
            }
        }
    }
}