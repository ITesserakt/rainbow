package ru.tesserakt.bot.rainbow.util

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.Event
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

inline fun <reified T : Event?> EventDispatcher.on(): Flux<T> = on(T::class.java)

operator fun <T1, T2> Tuple2<T1, T2>.component1() : T1 = t1
operator fun <T1, T2> Tuple2<T1, T2>.component2() : T2 = t2

operator fun <T1, T2, T3> Tuple3<T1, T2, T3>.component3(): T3 = t3

operator fun <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4>.component4(): T4 = t4

operator fun <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5>.component5(): T5 = t5

operator fun <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6>.component6(): T6 = t6

fun Guild.getRoleById(id : String): Mono<Role> {
    return getRoleById(Snowflake.of(id)).subscribeWithoutDisposable()
}

fun Guild.getRolesByName(name : String): Flux<Role> {
    return roles.filter { it.name == name }.subscribeWithoutDisposable()
}

fun <T> Mono<T>.subscribeWithoutDisposable() : Mono<T> {
    val buffer = this
    buffer.subscribe()
    return buffer
}

fun <T> Flux<T>.subscribeWithoutDisposable() : Flux<T> {
    val buffer = this
    buffer.subscribe()
    return buffer
}

fun launch(context : CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
        block.startCoroutine(Continuation(context) { result ->
            result.onFailure { ex ->
                val curThread = Thread.currentThread()
                curThread.uncaughtExceptionHandler.uncaughtException(curThread, ex)
            }
        })