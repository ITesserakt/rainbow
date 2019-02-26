package util

import discord4j.core.`object`.util.Snowflake
import java.awt.Color
import java.math.BigInteger
import java.util.*
import kotlin.random.Random

fun <T> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

operator fun String.times(num: Int): String =
        this.repeat(num)

infix fun Number.`**`(num : Number):Double =
        Math.pow(this.toDouble(), num.toDouble())

fun Throwable.prettyPrint() = StringBuilder(this.localizedMessage ?: "")
        .append(*this.stackTrace.take(10).toTypedArray())
        .toString()

fun String.toSnowflake(): Snowflake = Snowflake.of(this)

fun Long.toSnowflake(): Snowflake = Snowflake.of(this)

fun BigInteger.toSnowflake(): Snowflake = Snowflake.of(this)

val RandomColor
    get() = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))