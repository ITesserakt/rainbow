
import core.getParsedObject
import core.types.*
import modules.AdminsModule
import modules.HelpModule
import sx.blah.discord.handle.obj.IUser
import kotlin.reflect.KProperty

val VERSION : String by object {
    operator fun getValue(ref: Any?, property: KProperty<*>): String =
            getParsedObject<ConfigData>("src/main/resources/config.json").version
}

fun main(args: Array<String>) {
    ResolverService
            .bind(UserResolver(), IUser::class)
            .bind(LongResolver(), Long::class)
            .bind(BooleanResolver(), Boolean::class)
            .bind(IntResolver(), Int::class)
    AdminsModule()
    HelpModule()

    RegisterBot()
}
