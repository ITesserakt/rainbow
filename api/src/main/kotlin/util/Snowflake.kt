@file:Suppress("NOTHING_TO_INLINE")

package util

import discord4j.core.`object`.util.Snowflake
import org.jetbrains.annotations.Contract
import java.math.BigInteger

/**
 * @return new [Snowflake] represents [this]
 * @throws NumberFormatException If string does not match to number
 */
@Contract("!null -> new; null -> fail", pure = true)
inline fun String.toSnowflake(): Snowflake = Snowflake.of(this)

/**
 * @return new [Snowflake] represents [this]
 */
@Contract("!null -> new; null -> fail", pure = true)
inline fun Long.toSnowflake(): Snowflake = Snowflake.of(this)

/**
 * @return new [Snowflake] represents [this]
 */
@Contract("!null -> new; null -> fail", pure = true)
inline fun BigInteger.toSnowflake(): Snowflake = Snowflake.of(this)