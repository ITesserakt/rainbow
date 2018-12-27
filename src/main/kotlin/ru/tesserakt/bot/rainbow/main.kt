package ru.tesserakt.bot.rainbow

import ru.tesserakt.bot.rainbow.core.commands.CommandService
import ru.tesserakt.bot.rainbow.core.console.ConsoleService
import ru.tesserakt.bot.rainbow.core.types.*
import ru.tesserakt.bot.rainbow.modules.*
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser

fun main() {

    ResolverService
            .bind(UserResolver() to IUser::class)
            .bind(LongResolver() to Long::class)
            .bind(BooleanResolver() to Boolean::class)
            .bind(IntResolver() to Int::class)
            .bind(CharResolver() to Char::class)
            .bind(GuildResolver() to IGuild::class)
            .bind(StringResolver() to String::class)
            .bind(RoleResolver() to IRole::class)
            .bind(FloatResolver() to Float::class)
  
    CommandService
            .addModule(AdminsModule())
            .addModule(HelpModule())
            .addModule(SettingsModule())
            .addModule(RainbowModule())
    ConsoleService
            .addModule(ConsoleModule())

    RegisterBot()
}
