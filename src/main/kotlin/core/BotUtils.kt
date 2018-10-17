package core

import sx.blah.discord.handle.obj.IGuild

private val prefixes = HashMap<IGuild, Char>()

fun resolvePrefix(guild : IGuild) : Char? {
    return if(prefixes[guild] != null)
        prefixes[guild]
    else
        registerPrefix(guild, '/')
}

fun registerPrefix(guild: IGuild, prefix : Char) : Char {
    prefixes[guild] = prefix
    return prefix
}