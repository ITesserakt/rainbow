package command

import context.ICommandContext
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import util.Loggers
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

object CommandRegistry {
    private val providers = mutableListOf<CommandProvider<*>>()
    private val logger = Loggers.getLogger<CommandRegistry>()

    fun register(instance: KClass<ModuleBase<ICommandContext>>) {
        val instanceOfInstance = instance.createInstance()

        instance.declaredMemberFunctions.filterNot { it.findAnnotation<Command>() == null }
                .filter(::processFunc)
                .forEach { func ->
                    val provider = providers.find { it.type == instanceOfInstance.contextType }
                            ?: throw NoSuchElementException("Нет подходящего провайдера для контекста ${instanceOfInstance.contextType.simpleName}")

                    val name = processName(func)
                    val description = processDescription(func)
                    val permissions = processPermissions(func)
                    val aliases = processAliases(func)

                    provider.addCommand(CommandInfo(name, description, func, instanceOfInstance, PermissionSet.of(*permissions), aliases.toList()))
                }
    }

    private fun processAliases(func: KFunction<*>): Array<out String> =
            func.findAnnotation<Aliases>()?.aliases ?: emptyArray()

    private fun processPermissions(func: KFunction<*>): Array<out Permission> =
            func.findAnnotation<Permissions>()?.permissions ?: emptyArray()

    private fun processName(function: KFunction<*>): String = function.findAnnotation<Command>()!!.let {
        val result = if (it.name.isBlank()) function.name else it.name
        require(' ' !in result) { "Имя не должно быть пустым или содержать пробелов" }
        result
    }

    private fun processDescription(function: KFunction<*>): String = function.findAnnotation<Summary>()?.let {
        if (it.description.isBlank()) logger.warn("Нет описания для команды `${function.name}`")
        it.description
    } ?: "Описание отсутствует"

    private fun processFunc(function: KFunction<*>): Boolean {
        if (function.isOperator
                or function.isInline
                or function.isInfix
                or function.isExternal
                or function.isSuspend
                or function.isAbstract
        ) {
            logger.error("", IllegalStateException("Неверный тип функции"))
            return false
        }
        return true
    }

    fun addProvider(provider: CommandProvider<out ICommandContext>): CommandRegistry {
        providers.add(provider)
        return this
    }
}
