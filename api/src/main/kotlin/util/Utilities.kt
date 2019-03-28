package util

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
 * Shortcut to not [Optional.isPresent]
 */
val <T> Optional<T>.isNotPresent
    get() = !this.isPresent

/**
 * Uses for Python slices style
 * @return substring between this [indices]
 */
operator fun String.get(indices: IntRange) = this.slice(indices)