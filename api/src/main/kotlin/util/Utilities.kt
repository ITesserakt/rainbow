package util

import discord4j.core.event.domain.Event
import handler.Handler
import org.jetbrains.annotations.Contract
import reactor.core.publisher.Flux
import java.time.Duration
import java.util.*

/**
 * Repeats the string [num] times
 * Uses for Python style
 */
operator fun String.times(num: Int): String =
    this.repeat(num)

/**
 * Uses for Python power style
 */
infix fun Number.`**`(num: Number): Double =
    Math.pow(this.toDouble(), num.toDouble())

/**
 * PT3M-5S is too ugly, 3M 5S is better ☻
 */
fun Duration.toPrettyString(): String = this.toString()
    .drop(3)
    .replace('-', ' ')

/**
 * Shortcut to not [Optional.isPresent]
 */
val <T> Optional<T>.isNotPresent
    get() = !this.isPresent

/**
 * Uses for Python slices style
 * @return substring between this [indices]
 */
operator fun String.get(indices: IntRange) = this.slice(indices)

/**
 * Uses for C# event style
 */
operator fun <T : Event> Flux<T>.plusAssign(handler: Handler<T>) {
    this.subscribe { handler.handle(it) }
}

/**
 * Clams [T] between [min] and [max] exclusive
 * @return clamped [T]
 */
@Contract(pure = true)
fun <T> T.clamp(min: T, max: T): T where T : Number, T : Comparable<T> {
    if (this < min) return min
    if (this > max) return max
    return this
}