package core.console

import core.commands.Command
import core.commands.CommandBuilder

object ConsoleService {
    private val commands = HashMap<String, Command>()

    fun addCommand(builder : CommandBuilder.() -> Command) {
        val command = builder.invoke(CommandBuilder())
        commands[command.name] = command
    }

    fun getCommandByName(name: String): Command? = commands[name]

    fun getCommandByAlias(param : String) : Command? {
        for(command in commands.values)
            for (alias in command.aliases)
                if(alias == param)
                    return command
        return null
    }
}
