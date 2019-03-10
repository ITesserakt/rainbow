package command

import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation

abstract class CommandProvider <T : ICommandContext> {
    private val commands_ = hashMapOf<String, CommandInfo>()
    val commands: Array<CommandInfo>
        get() = commands_.values.toTypedArray()

    open fun find(name: String): CommandInfo? = commands_.entries.find { it.key == name }?.value
    open fun addCommand(command: CommandInfo) {
        commands_[command.name] = command
    }

    inline fun <reified Module : ModuleBase<T>> registerModule(): CommandProvider<T> = apply {
        Module::class.declaredFunctions
                .filter { it.findAnnotation<Command>() != null }
                .map { func: KFunction<*> ->
                    val commandAnnotation = func.findAnnotation<Command>()!!
                    val name = if (commandAnnotation.name.isBlank()) func.name else commandAnnotation.name
                    val description = func.findAnnotation<Summary>()?.description ?: "Описание отсутствует"
                    val permissions = func.findAnnotation<Permissions>()?.permissions ?: arrayOf()
                    addCommand(CommandInfo(name, description, func, Module::class.createInstance(), PermissionSet.of(*permissions)))
                }
    }
}
