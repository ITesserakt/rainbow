package command

import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation

object CommandRegistry {
    private val providers = mutableListOf<CommandProvider<*>>()

    fun register(instance: KClass<ModuleBase<ICommandContext>>) {
        val instanceOfInstance = instance.createInstance()

        instance.declaredMemberFunctions.filterNot { it.findAnnotation<Command>() == null }
                .forEach { func ->
                    val provider = providers.find { it.type == instanceOfInstance.contextType }
                            ?: throw NoSuchElementException("Нет подходящего провайдера для контекста ${instanceOfInstance.contextType.simpleName}")

                    val commandAnn = func.findAnnotation<Command>()!!
                    val name = if (commandAnn.name.isBlank()) func.name else commandAnn.name
                    val description = func.findAnnotation<Summary>()?.description ?: "Описание отсутствует"
                    val permissions = func.findAnnotation<Permissions>()?.permissions ?: arrayOf()
                    provider.addCommand(CommandInfo(name, description, func, instanceOfInstance, PermissionSet.of(*permissions)))
                }
    }

    fun addProvider(provider: CommandProvider<out ICommandContext>): CommandRegistry {
        providers.add(provider)
        return this
    }
}
