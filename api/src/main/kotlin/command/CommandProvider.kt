package command

import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import reactor.core.publisher.toFlux
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation

abstract class CommandProvider <T : ICommandContext> {
    abstract val commands : Array<CommandInfo>
    abstract val modules : Array<ModuleBase<T>>

    abstract fun find(name : String) : CommandInfo?
    abstract fun addCommand(command : CommandInfo)

    open fun addModule(module : ModuleBase<T>) : CommandProvider<T> {
        module::class.declaredFunctions.toFlux()
                .parallel()
                .filter { it.findAnnotation<Command>() != null }
                .map {
                    val commandAnnotation = it.findAnnotation<Command>()!!
                    val name = if(commandAnnotation.name.isBlank()) it.name else commandAnnotation.name
                    val description = it.findAnnotation<Summary>()?.description ?: ""
                    val permissions = it.findAnnotation<Permissions>()?.permissions ?: arrayOf()

                    addCommand(CommandBuilder.createNew()
                            .setName(name)
                            .setFuncPointer(it)
                            .setModulePointer(module)
                            .setDescription(description)
                            .setParams(it.parameters.toTypedArray())
                            .setPermissions(PermissionSet.of(*permissions))
                            .build())
                }.subscribe()
        return this
    }
}
