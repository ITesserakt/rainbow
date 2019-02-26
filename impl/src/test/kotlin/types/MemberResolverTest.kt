package types

import discord4j.core.`object`.entity.Member
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError
import util.toSnowflake

class MemberResolverTest : ResolverTestTemplate<Member>() {
    override val resolver: ITypeResolver<Member> = MemberResolver()
    private val id = 316249690092077065L.toSnowflake()

    @Test
    fun `mention, parse to member entity, Mono(member)`() {
        StepVerifier.create(resolver.read(fakeContext, "<&316249690092077065>"))
                .assertNext { assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `non-existent mention, parse to member entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "<&45435315>"))
                .expectError()
                .verify()
    }

    @Test
    fun `id, parse to member entity, Mono(member)`() {
        StepVerifier.create(resolver.read(fakeContext, "316249690092077065"))
                .assertNext { assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `non-existent id, parse to member entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "42453245"))
                .expectError()
                .verify()
    }

    @Test
    fun `name, parse to member entity, Mono(member)`() {
        StepVerifier.create(resolver.read(fakeContext, "VoV4ik"))
                .expectNextCount(1)
                .verifyComplete()
    }

    @Test
    fun `something terrible, parse to member entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "g5t48th43h2t34t34h9th9438ut893"))
                .expectError(NoSuchElementException::class)
                .verify()
    }
}