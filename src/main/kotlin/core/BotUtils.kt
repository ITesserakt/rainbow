package core

import CURRENTDIR
import sx.blah.discord.handle.obj.IGuild

object Prefix {
    internal var prefixes = HashMap<String, String>()

    /**
     * Возвращает зарегистрированный префикс или стандартный
     * @param guild объект гильдии
     */
    fun resolve(guild: IGuild): Char {
        return resolvePrefix(guild.stringID)
    }

    /**
     * Сохраняет префикс для указанной гильдии
     * @param guild объект гильдии
     * @param prefix префикс
     */
    fun register(guild: IGuild, prefix: Char) {
        registerPrefix(guild.stringID, prefix)
    }

    private fun registerPrefix(guildId: String, prefix: Char): String {
        prefixes[guildId] = prefix.toString()
        Loader.save()
        return prefix.toString()
    }

    private fun resolvePrefix(guildId: String): Char {
        return (prefixes[guildId] ?: registerPrefix(guildId, '/'))[0]
    }

    object Loader {
        fun save() {
            writeToJSON("$CURRENTDIR/resources/prefixes.json", prefixes)
        }

        internal fun load() {
            prefixes = getParsedObject("$CURRENTDIR/resources/prefixes.json")
        }
    }
}