package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.ICommandContext
import ru.tesserakt.bot.rainbow.core.IService
import ru.tesserakt.bot.rainbow.core.commands.CommandInfo
import ru.tesserakt.bot.rainbow.core.commands.Remainder
import ru.tesserakt.bot.rainbow.core.commands.RequireLogin
import ru.tesserakt.bot.rainbow.core.types.ResolverService
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.PermissionUtils
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

abstract class Handler {
    protected fun runCommand(command : CommandInfo, context : ICommandContext) {
        command.parentModule.setContextInternal(context)

        val reqLogin = command.funObj.findAnnotation<RequireLogin>() != null
        if(reqLogin && !command.parentModule.updateLateInitProps())
            return

        val params = mutableMapOf<KParameter, Any?>()

        for (param in command.parameters) {
            val isRemainder = param.findAnnotation<Remainder>() != null
            val type = param.type.classifier as KClass<*>

            if(type == command.parentModule::class) {
                params[param] = command.parentModule
                continue
            }

            if (param.isOptional)
                params[param] = (ResolverService.parseOptional(type, context, param.index - 1, isRemainder)) ?: continue
            else
                params[param] = (ResolverService.parse(type, context, param.index - 1, isRemainder))
        }
        command.funObj.callBy(params.filter { it.value != null })
    }

    protected fun getCommand(service : IService<*>, name : String) : CommandInfo? {
        return service.getCommandByName(name) ?: return service.getCommandByAlias(name)
    }

    protected fun checkPermissions(perms: Array<out Permissions>, context: ICommandContext): Boolean {
        return when {
            perms.isEmpty() -> true
            PermissionUtils.hasPermissions(context.guild, context.user, perms.toEnumSet()) -> true
            else -> throw IllegalAccessException("Не хватает прав! (${perms.joinToString { it.name }})")
        }
    }

    private fun <T : Enum<T>?> Array<out T>.toEnumSet(): EnumSet<T> = EnumSet.copyOf(this.toList())
}