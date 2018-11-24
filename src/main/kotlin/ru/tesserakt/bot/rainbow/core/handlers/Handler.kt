package ru.tesserakt.bot.rainbow.core.handlers

import ru.tesserakt.bot.rainbow.core.ICommandContext
import ru.tesserakt.bot.rainbow.core.commands.CommandInfo
import ru.tesserakt.bot.rainbow.core.commands.CommandService
import ru.tesserakt.bot.rainbow.core.commands.Remainder
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
        command.funObj.callBy(params)
    }

    protected fun getCommand(name : String) : CommandInfo? {
        return CommandService.getCommandByName(name) ?: return CommandService.getCommandByAlias(name)
    }

    protected fun checkPermissions(perms: Array<out Permissions>, context: ICommandContext): Boolean {
        if (perms.isEmpty())
            return true
        if (PermissionUtils.hasPermissions(context.guild, context.user, perms.toEnumSet()))
            return true
        else throw IllegalAccessException("Доступ запрещен")
    }

    private fun <T : Enum<T>?> Array<out T>.toEnumSet(): EnumSet<T> = EnumSet.copyOf(this.toList())
}