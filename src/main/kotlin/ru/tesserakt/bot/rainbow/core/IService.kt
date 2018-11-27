package ru.tesserakt.bot.rainbow.core

import ru.tesserakt.bot.rainbow.core.commands.*
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

interface IService<T : ICommandContext> {
    val commandsList : Collection<CommandInfo>
    fun getCommandByName(name: String): CommandInfo?
    fun getCommandByAlias(param : String) : CommandInfo?
    fun addCommand(init : CommandBuilder.() -> CommandInfo)
    fun addModule(module : ModuleBase<T>) : IService<T> {
        module::class.declaredMemberFunctions.filter { it.findAnnotation<Command>() != null }.forEach {
            val aliases = it.findAnnotation<Aliases>()?.alias ?: arrayOf()
            val commandAnnotation = it.findAnnotation<Command>()!!
            val name = if(commandAnnotation.name.isBlank()) it.name else commandAnnotation.name
            val summary = it.findAnnotation<Summary>()?.description ?: ""
            val restrictions = it.findAnnotation<Restrictions>()?.permissions ?: arrayOf()

            it.isAccessible = true

            addCommand {
                this.name = name
                this.summary = summary
                this.aliases = aliases
                this.parameters = it.parameters.toTypedArray()
                this.restrictions = restrictions
                this.parentModule = module
                funObject = it
                build()
            }
        }

        return this
    }
}
