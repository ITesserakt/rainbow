package command

import context.ICommandContext
import util.Loggers
import kotlin.reflect.KClass

abstract class CommandProvider<T : ICommandContext>(val type: KClass<T>) {
    private val logger = Loggers.getLogger<CommandProvider<T>>()

    private val _commands = hashMapOf<Array<String>, CommandInfo>()
    val commands: Array<CommandInfo>
        get() = _commands.values.filter { !it.isHidden }.toTypedArray()

    open fun find(name: String): CommandInfo? = _commands.entries.find { name in it.key }?.value

    internal open fun addCommand(command: CommandInfo) {
        if (find(command.name) == null || command.aliases.map { find(it) }.all { it == null }) {
            _commands[arrayOf(*command.aliases.toTypedArray(), command.name)] = command
        } else {
            logger.error("Команда с таким названием уже зарегистрирована")
        }
    }
}
