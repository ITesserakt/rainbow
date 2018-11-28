package ru.tesserakt.bot.rainbow

import ru.tesserakt.bot.rainbow.core.Database

object ConfigData {
    val version : String
    val token : String

    init {
        version = configQuarry("version")
        token = configQuarry("token")
        Database.disconnect()
    }

    private fun configQuarry(ref : String) : String {
        val result = Database.execute("SELECT value FROM config WHERE key='$ref'")
        result.next()
        return result.getString(1)
    }
}