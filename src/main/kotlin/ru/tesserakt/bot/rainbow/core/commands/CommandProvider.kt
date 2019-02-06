package ru.tesserakt.bot.rainbow.core.commands

import reactor.core.publisher.toFlux
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.context.ICommandContext
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

abstract class CommandProvider <T : ICommandContext> {
    abstract val commands: Array<Command>
    abstract val modules: Array<ModuleBase<T>>

    abstract fun find(name: String): Command?
    internal abstract fun addCommand(builder: CommandBuilder.() -> Command)

    protected open fun addModule(module: ModuleBase<T>): CommandProvider<T> {
        module::class.declaredFunctions.toFlux()
                .filter { it.findAnnotation<CommandAnn>() != null }
                .doOnNext { it.isAccessible = true }
                .map {
                    val commandAnn = it.findAnnotation<CommandAnn>()!!
                    val aliases = it.findAnnotation<Aliases>()?.aliases ?: arrayOf()
                    val name = if (commandAnn.name.isBlank()) it.name else commandAnn.name
                    val description = it.findAnnotation<Summary>()?.description ?: ""
                    val perms = it.findAnnotation<Permissions>()?.permissions ?: arrayOf()

                    addCommand {
                        setName(name)
                        setAliases(*aliases)
                        setDescription(description)
                        setPermissions(*perms)

                        setParams(*it.parameters.toTypedArray())
                        setParentModule(module)
                        setParentFunc(it)
                        build()
                    }
                }.subscribe()
        return this
    }
}