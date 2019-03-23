package util

import reactor.bool.BooleanUtils
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.util.function.Tuple2
import java.awt.Color
import java.time.Duration
import java.util.*
import kotlin.random.Random

fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

operator fun String.times(num: Int): String =
        this.repeat(num)

infix fun Number.`**`(num: Number): Double =
        Math.pow(this.toDouble(), num.toDouble())

inline val RandomColor
    get() = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

fun <T1 : Any, T2 : Any> Mono<T1>.zipWith(other: T2): Mono<Tuple2<T1, T2>> =
        this.zipWith(other.toMono())

fun Duration.toPrettyString(): String = this.toString()
    .drop(3)
        .replace('-', ' ')

infix fun Mono<Boolean>.and(other: Mono<Boolean>): Mono<Boolean> = BooleanUtils.and(this, other)