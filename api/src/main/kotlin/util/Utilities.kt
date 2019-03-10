package util

import discord4j.core.`object`.util.Snowflake
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import reactor.util.function.Tuple2
import java.awt.Color
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

operator fun String.times(num: Int): String =
        this.repeat(num)

infix fun Number.`**`(num: Number): Double =
        Math.pow(this.toDouble(), num.toDouble())

fun String.toSnowflake(): Snowflake = Snowflake.of(this)

fun Long.toSnowflake(): Snowflake = Snowflake.of(this)

fun BigInteger.toSnowflake(): Snowflake = Snowflake.of(this)

val RandomColor
    get() = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))

fun <T1 : Any, T2 : Any> Mono<T1>.zipWith(other: T2): Mono<Tuple2<T1, T2>> =
        this.zipWith(other.toMono())