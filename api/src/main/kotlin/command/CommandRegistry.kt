package command

import command.processors.*
import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import util.hasAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions

object CommandRegistry {
    private val providers = mutableListOf<CommandProvider<*>>()

    fun register(instance: KClass<ModuleBase<ICommandContext>>) {
        val instanceOfInstance = instance.createInstance()

        instance.declaredMemberFunctions
                .filter { it.hasAnnotation<Command>() }
                .filter { FunctionProcessor().process(it) }
                .forEach { func ->
                    val provider = providers.find { it.type == instanceOfInstance.contextType }
                            ?: throw NoSuchElementException("Нет подходящего провайдера для контекста ${instanceOfInstance.contextType.simpleName}")

                    val name = NameProcessor(instance).process(func)
                    val description = DescriptionProcessor().process(func)
                    val permissions = PermissionsProcessor().process(func)
                    val aliases = AliasesProcessor().process(func)

                    provider.addCommand(CommandInfo(name, description, func, instanceOfInstance, PermissionSet.of(*permissions), aliases.toList()))
                }
    }

    fun addProvider(provider: CommandProvider<out ICommandContext>): CommandRegistry {
        providers.add(provider)
        return this
    }
}
