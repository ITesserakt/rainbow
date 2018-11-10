
import core.Prefix
import core.types.*
import modules.*
import sx.blah.discord.handle.obj.*

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
  
    AdminsModule()
    HelpModule()
    SettingsModule()
    ConsoleModule()
    RainbowModule()

    Prefix.Loader.load()

    RegisterBot()
}
