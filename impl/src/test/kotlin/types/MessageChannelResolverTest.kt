package types

import discord4j.core.`object`.entity.MessageChannel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import util.toSnowflake

internal class MessageChannelResolverTest : types.ResolverTestTemplate<MessageChannel>() {
    override val resolver: ITypeResolver<MessageChannel> = MessageChannelResolver()
    private val id = 490951935894093858.toSnowflake()

    @Test
    fun `mention, parse to channel entity, Mono(mChannel)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "<&490951935894093858>").id, id)
    }

    @Test
    fun `non-existent mention, parse to channel entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "<&2549456125498458484>")
            }
        }
    }

    @Test
    fun `id, parse to channel entity, Mono(mChannel)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "490951935894093858").id, id)
    }

    @Test
    fun `non-existent id, parse to channel entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "<&2549456125498458484>")
            }
        }
    }

    @Test
    fun `name, parse to channel entity, Mono(mChannel)`() = runBlocking {
        Assertions.assertEquals(resolver.read(fakeContext, "основной").id, id)
    }

    @Test
    fun `something terrible, parse to channel entity, expect error`() {
        Assertions.assertThrows(Exception::class.java) {
            runBlocking {
                resolver.read(fakeContext, "gjui4hguhjoei")
            }
        }
    }
}