package ru.tesserakt.bot.rainbow.util

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.Event
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuple3
import reactor.util.function.Tuple4
import reactor.util.function.Tuple5

inline fun <reified T : Event?> EventDispatcher.on(): Flux<T> = on(T::class.java)

operator fun <T1, T2> Tuple2<T1, T2>.component1() : T1 = this.t1
operator fun <T1, T2> Tuple2<T1, T2>.component2() : T2 = this.t2

operator fun <T1, T2, T3> Tuple3<T1, T2, T3>.component3(): T3 = this.t3

operator fun <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4>.component4(): T4 = this.t4

operator fun <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5>.component5(): T5 = this.t5

fun Mono<Guild>.getRoleById(id : String): Mono<Role> {
    val role = this.flatMap {
        it.getRoleById(Snowflake.of(id))
    }
    role.subscribe()
    return role
}

fun Mono<Guild>.getRolesByName(name : String): Flux<Role> {
    val roles = this.flux().flatMap { guild ->
        guild.roles.filter { it.name == name }
    }
    roles.subscribe()
    return roles
}
