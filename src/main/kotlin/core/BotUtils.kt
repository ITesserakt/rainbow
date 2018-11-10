package core

import ConfigData
import sx.blah.discord.handle.obj.IGuild
import java.io.File

val RESOURCES: String = if (File("D:\\JetBrains\\rainbow\\src\\main\\resources").exists())
    "D:\\JetBrains\\rainbow\\src\\main\\resources"
else
    ""
val VERSION : String = getParsedObject<ConfigData>("$RESOURCES\\config.json").version

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
            writeToJSON("$RESOURCES/prefixes.json", prefixes)
        }

        internal fun load() {
            prefixes = getParsedObject("$RESOURCES/prefixes.json")
        }
    }
}