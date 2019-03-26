package command

import context.ICommandContext
import util.Loggers
import kotlin.reflect.KClass

/**
 * Base provider for all commands
 * @param type baked type of context (because type erasure)
 * @param T type of context
 */
abstract class CommandProvider<T : ICommandContext>(val type: KClass<T>) {
    private val logger = Loggers.getLogger<CommandProvider<T>>()

    private val _commands = hashMapOf<List<String>, CommandInfo>()

    /**
     * Contains all commands except hidden
     */
    val commands: Array<CommandInfo>
        get() = _commands.values.filter { !it.isHidden }.toTypedArray()

    /**
     * Gives command by name or alias
     */
    open fun find(name: String): CommandInfo? = _commands.entries.find { name in it.key }?.value

    internal fun addCommand(command: CommandInfo) {
        if (find(command.name) == null || command.aliases.map { find(it) }.all { it == null }) {
            val key = command.aliases + command.name
            _commands[key] = command
        } else {
            logger.error("Команда с таким названием уже зарегистрирована")
        }
    }
}
