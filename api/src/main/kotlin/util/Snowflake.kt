@file:Suppress("NOTHING_TO_INLINE")

package util

import discord4j.core.`object`.util.Snowflake
import java.math.BigInteger

inline fun String.toSnowflake(): Snowflake = Snowflake.of(this)

inline fun Long.toSnowflake(): Snowflake = Snowflake.of(this)

inline fun BigInteger.toSnowflake(): Snowflake = Snowflake.of(this)