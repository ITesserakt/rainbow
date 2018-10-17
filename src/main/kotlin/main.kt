import core.getParsedObject
import core.types.LongResolver
import core.types.ResolverService
import core.types.UserResolver
import modules.AdminsModule
import modules.HelpModule
import kotlin.reflect.KProperty

val VERSION : String by object {
    operator fun getValue(ref: Any?, property: KProperty<*>): String =
            getParsedObject<ConfigData>("src/main/resources/config.json").version
}

fun main(args: Array<String>) {
    AdminsModule()
    HelpModule()
    ResolverService.addResolver(UserResolver())
    ResolverService.addResolver(LongResolver())
    RegisterBot()
}