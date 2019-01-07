package ru.tesserakt.bot.rainbow.util

private val db = Database
private fun configQuarryFor(key : String) = "SELECT value FROM config WHERE key='$key'"

val token = db.execute<String>(configQuarryFor("token"))
val version = db.execute<String>(configQuarryFor("version"))
val debug = db.execute<String>(configQuarryFor("debug_mode")).toBoolean()