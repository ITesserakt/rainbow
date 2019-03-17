package command

import context.ICommandContext
import kotlin.reflect.KClass

abstract class CommandProvider <T : ICommandContext> {
    abstract val type: KClass<T>
    private val commands_ = hashMapOf<Array<String>, CommandInfo>()
    val commands: Array<CommandInfo>
        get() = commands_.values.toTypedArray()

    open fun find(name: String): CommandInfo? = commands_.entries.find { name in it.key }?.value
    internal open fun addCommand(command: CommandInfo) {
        commands_[arrayOf(*command.aliases.toTypedArray(), command.name)] = command
    }
}
