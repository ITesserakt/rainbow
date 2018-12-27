package ru.tesserakt.bot.rainbow

import ru.tesserakt.bot.rainbow.core.Database
import ru.tesserakt.bot.rainbow.generated.tables.Config.CONFIG

object ConfigData {
    @JvmStatic
    val version : String
    @JvmStatic
    val token : String
    @JvmStatic
    val debug : Boolean

    init {
        version = configQuarryFor("version")
        token = configQuarryFor("token")
        debug = configQuarryFor("debug_mode").toBoolean()
    }

    @JvmStatic
    private fun configQuarryFor(ref : String) : String = Database.connect {
        select(CONFIG.VALUE)
                .from(CONFIG)
                .where(CONFIG.KEY.eq(ref))
                .fetch(CONFIG.VALUE)[0]
    }
}