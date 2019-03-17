package command

import context.ICommandContext
import util.Loggers
import kotlin.reflect.KClass

abstract class CommandProvider<T : ICommandContext>(val type: KClass<T>) {
    private val logger = Loggers.getLogger<CommandProvider<T>>()

    private val commands_ = hashMapOf<Array<String>, CommandInfo>()
    val commands: Array<CommandInfo>
        get() = commands_.values.toTypedArray()

    open fun find(name: String): CommandInfo? = commands_.entries.find { name in it.key }?.value
    internal open fun addCommand(command: CommandInfo) {
        if (find(command.name) == null || command.aliases.map { find(it) }.all { it == null }) {
            commands_[arrayOf(*command.aliases.toTypedArray(), command.name)] = command
        } else {
            logger.error("Команда с таким названием уже зарегестрирована")
        }
    }
}
