package ru.tesserakt.bot.rainbow.util

private val db = Database
private fun configQuarryFor(key : String) = "SELECT value FROM config WHERE key='$key'"

val token = db.execute<String>(configQuarryFor("token"))
const val version = "0.0.7-prerelease"
val debug = db.execute<String>(configQuarryFor("debug_mode")).toBoolean()