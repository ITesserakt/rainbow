package types

import discord4j.core.`object`.entity.Role
import org.junit.jupiter.api.Test
import reactor.test.StepVerifier
import reactor.test.expectError

internal class RoleResolverTest : ResolverTestTemplate<Role>() {
    override val resolver: ITypeResolver<Role> = RoleResolver()

    @Test
    fun `non-existing mention but regex is true`() {
        StepVerifier.create(resolver.read(fakeContext, "<@&333333333333333333>"))
                .expectError(NoSuchElementException::class)
                .verify()
    }

    @Test
    fun `normal id, parse to role entity and expect Mono(role)`() {
        StepVerifier.create(resolver.read(fakeContext, "510459822819246091"))
                .assertNext {
                    assertEquals(it.id.asLong(), 510459822819246091)
                }.verifyComplete()
    }

    @Test
    fun `role name, parse to role entity and expect Mono(role)`() {
        StepVerifier.create(resolver.read(fakeContext, "Admins"))
                .assertNext {
                    assertEquals(it.id.asLong(), 510459822819246091)
                }.verifyComplete()
    }

    @Test
    fun `invalid name, parse to role entity and expect error`() {
        StepVerifier.create(resolver.read(fakeContext, "i`m not exist!"))
                .expectError()
                .verify()
    }
}