package core.commands

import java.util.*

object CommandService {
    private val commandsMap = HashMap<String, Command>()
    val commandsList: MutableCollection<Command> = commandsMap.values

    fun addCommand(init: CommandBuilder.() -> Command) {
        val command = init.invoke(CommandBuilder())
        commandsMap[command.name] = command
    }

    fun getCommandByName(name: String) = commandsMap[name]

    fun getCommandByAlias(input: String): Command? {
        for (command in commandsList)
            for (alias in command.aliases)
                if (alias == input)
                    return command
        return null
    }
}