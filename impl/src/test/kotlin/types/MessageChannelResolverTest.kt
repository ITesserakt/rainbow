package types

import discord4j.core.`object`.entity.MessageChannel
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError
import util.toSnowflake

internal class MessageChannelResolverTest : types.ResolverTestTemplate<MessageChannel>() {
    override val resolver: ITypeResolver<MessageChannel> = MessageChannelResolver()
    private val id = 490951935894093858.toSnowflake()

    @Test
    fun `mention, parse to channel entity, Mono(mChannel)`() {
        StepVerifier.create(resolver.read(fakeContext, "<#490951935894093858>"))
                .assertNext { assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `non-existent mention, parse to channel entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "<#45435315>"))
                .expectError()
                .verify()
    }

    @Test
    fun `id, parse to channel entity, Mono(mChannel)`() {
        StepVerifier.create(resolver.read(fakeContext, "490951935894093858"))
                .assertNext { assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `non-existent id, parse to channel entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "42453245"))
                .expectError()
                .verify()
    }

    @Test
    fun `name, parse to channel entity, Mono(mChannel)`() {
        StepVerifier.create(resolver.read(fakeContext, "основной"))
                .assertNext { assertEquals(it.id, 490951935894093858.toSnowflake()) }
                .verifyComplete()
    }

    @Test
    fun `something terrible, parse to channel entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "g5t48th43h2t34t34h9th9438ut893"))
                .expectError(NoSuchElementException::class)
                .verify()
    }
}