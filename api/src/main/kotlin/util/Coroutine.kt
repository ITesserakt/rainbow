package util

import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Snowflake
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

suspend fun <T> Mono<T>.await(): T = this.awaitSingle()

suspend fun <T> Mono<T>.awaitOrNull(): T? = this.awaitFirstOrNull()

suspend fun <T> Flux<T>.awaitMany(): List<T> = this.collectList().awaitFirstOrElse { emptyList() }

suspend fun Member.isHigherAsync(id: Snowflake): Boolean = this.isHigher(id).await()

suspend fun Member.isHigherAsync(otherMember: Member): Boolean = this.isHigher(otherMember).await()