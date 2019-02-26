package types

import discord4j.core.`object`.entity.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.test.StepVerifier
import reactor.test.expectError
import util.toSnowflake

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class UserResolverTest : ResolverTestTemplate<User>() {
    override val resolver: ITypeResolver<User> = UserResolver()
    private val id = 316249690092077065L.toSnowflake()

    @Test
    fun `mention, parse to user entity, Mono(user)`() {
        StepVerifier.create(resolver.read(fakeContext, "<&316249690092077065>"))
                .assertNext { assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `non-existent mention, parse to user entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "<&45435315>"))
                .expectError()
                .verify()
    }

    @Test
    fun `id, parse to user entity, Mono(user)`() {
        StepVerifier.create(resolver.read(fakeContext, "316249690092077065"))
                .assertNext { assertEquals(it.id, id) }
                .verifyComplete()
    }

    @Test
    fun `non-existent id, parse to user entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "42453245"))
                .expectError()
                .verify()
    }

    //Не проходит, потому что нельзя заблочить client.login() и client.users пустой
    @Disabled
    @Test
    fun `name with discriminator, parse to user entity, Mono(user)`() {
        StepVerifier.create(resolver.read(fakeContext, "VoV4ik#5413"))
                .expectNextCount(1)
                .verifyComplete()
    }

    @Test
    fun `something terrible, parse to user entity, expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "g5t48th43h2t34t34h9th9438ut893"))
                .expectError(NoSuchElementException::class)
                .verify()
    }
}