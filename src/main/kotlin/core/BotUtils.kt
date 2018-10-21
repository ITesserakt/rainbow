package core

import sx.blah.discord.handle.obj.IGuild

private val prefixes = HashMap<Long, Char>()

/**
 * Возвращает зарегистрированный префикс или стандартный
 * @param guild объект гильдии
 */
fun resolvePrefix(guild : IGuild) : Char {
    return resolvePrefix(guild.longID)
}

/**
 * Сохраняет префикс для указанной гильдии
 * @param guild объект гильдии
 * @param prefix префикс
 */
fun registerPrefix(guild: IGuild, prefix : Char) {
    registerPrefix(guild.longID, prefix)
}

internal fun registerPrefix(guildId : Long, prefix: Char) : Char {
    prefixes[guildId] = prefix
    return prefix
}

internal fun resolvePrefix(guildId : Long) : Char {
    return prefixes[guildId] ?: registerPrefix(guildId, '/')
}