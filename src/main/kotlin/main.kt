
import core.getParsedObject
import core.types.*
import modules.AdminsModule
import modules.HelpModule
import modules.SettingsModule
import sx.blah.discord.handle.obj.IUser
import java.io.File
import kotlin.reflect.KProperty

val VERSION : String by object {
    operator fun getValue(ref: Any?, property: KProperty<*>): String =
            getParsedObject<ConfigData>("src/main/resources/config.json").version
}

val CURRENTDIR = "${File("").absolutePath}/src/main"
val CURRENTTESTDIR = "${File("").absolutePath}/src/test"

fun main(args: Array<String>) {
    ResolverService
            .bind(UserResolver(), IUser::class)
            .bind(LongResolver(), Long::class)
            .bind(BooleanResolver(), Boolean::class)
            .bind(IntResolver(), Int::class)
            .bind(CharResolver(), Char::class)
    AdminsModule()
    HelpModule()
    SettingsModule()

    RegisterBot()
}
