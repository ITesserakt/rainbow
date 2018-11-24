package ru.tesserakt.bot.rainbow.core.console

import ru.tesserakt.bot.rainbow.core.IService
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.CommandBuilder
import ru.tesserakt.bot.rainbow.core.commands.CommandInfo

object ConsoleService : IService<ConsoleCommandContext> {
    private val commands = HashMap<String, CommandInfo>()
    override val commandsList: Collection<CommandInfo> = commands.values
    internal val modules = ArrayList<ModuleBase<ConsoleCommandContext>>()

    override fun addModule(module: ModuleBase<ConsoleCommandContext>): IService<ConsoleCommandContext> {
        modules.add(module)
        return super.addModule(module)
    }

    override fun addCommand(init : CommandBuilder.() -> CommandInfo) {
        val command = CommandBuilder().init()
        commands[command.name] = command
    }

    override fun getCommandByName(name: String): CommandInfo? = commands[name]

    override fun getCommandByAlias(param : String) : CommandInfo? {
        for(command in commands.values)
            for (alias in command.aliases)
                if(alias == param)
                    return command
        return null
    }
}
