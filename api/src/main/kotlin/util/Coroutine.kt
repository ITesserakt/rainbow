package util

import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Snowflake
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Suspends execution to get value of this [Mono] or throws exception if the [Mono] produces error
 * @throws NoSuchElementException If the [Mono] does not contain any value
 */
suspend fun <T> Mono<T>.await(): T = this.awaitSingle()

/**
 * Suspends execution to get value of this [Mono] or throws exception if the [Mono] produces error
 */
suspend fun <T> Mono<T>.awaitOrNull(): T? = this.awaitFirstOrNull()

/**
 * Suspends execution to get values of this [Flux] collected to list or throws exception if the [Flux] produces error
 * @return [List] of [T] or empty [List] if [Flux] is empty
 */
suspend fun <T> Flux<T>.awaitMany(): List<T> = this.collectList().awaitFirstOrElse { emptyList() }

/**
 * Requests to determine if this member is higher in the role hierarchy than the member as represented
 * by the supplied ID or signal IllegalArgumentException if the member as represented by the supplied ID is in
 * a different guild than this member.
 * This is determined by the positions of each of the members' highest roles.
 *
 * @param id The ID of the member to compare in the role hierarchy with this member.
 * @return A [Mono] where, upon successful completion, emits `true` if this member is higher in the role
 * hierarchy than the member as represented by the supplied ID, `false` otherwise. If an error is received,
 * it is emitted through the [Mono].
 */
suspend fun Member.isHigherAsync(id: Snowflake): Boolean = this.isHigher(id).await()

/**
 * Requests to determine if this member is higher in the role hierarchy than the provided member or signal
 * IllegalArgumentException if the provided member is in a different guild than this member.
 * This is determined by the positions of each of the members' highest roles.
 *
 * @param otherMember The member to compare in the role hierarchy with this member.
 * @return A [Mono] where, upon successful completion, emits `true` if this member is higher in the
 * role hierarchy than the provided member, `false` otherwise. If an error is received, it is emitted
 * through the [Mono].
 */
suspend fun Member.isHigherAsync(otherMember: Member): Boolean = this.isHigher(otherMember).await()