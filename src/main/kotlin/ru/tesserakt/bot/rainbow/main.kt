
import ru.tesserakt.bot.rainbow.core.Prefix
import ru.tesserakt.bot.rainbow.core.commands.CommandService
import ru.tesserakt.bot.rainbow.core.console.ConsoleService
import ru.tesserakt.bot.rainbow.core.types.*
import ru.tesserakt.bot.rainbow.modules.*
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser

fun main(args: Array<String>) {

    ResolverService
            .bind(UserResolver(), IUser::class)
            .bind(LongResolver(), Long::class)
            .bind(BooleanResolver(), Boolean::class)
            .bind(IntResolver(), Int::class)
            .bind(CharResolver(), Char::class)
            .bind(GuildResolver(), IGuild::class)
            .bind(StringResolver(), String::class)
            .bind(RoleResolver(), IRole::class)
            .bind(FloatResolver(), Float::class)
  
    CommandService
            .addModule(AdminsModule())
            .addModule(HelpModule())
            .addModule(SettingsModule())
            .addModule(RainbowModule())
    ConsoleService
            .addModule(ConsoleModule())

    Prefix.Loader.load()

    RegisterBot()
}
