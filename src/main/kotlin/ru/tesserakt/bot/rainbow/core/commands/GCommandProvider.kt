package ru.tesserakt.bot.rainbow.core.commands

import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.context.GCommandContext

object GCommandProvider : CommandProvider<GCommandContext>() {
    override var commands = arrayOf<Command>()
        private set

    override var modules = arrayOf<ModuleBase<GCommandContext>>()
        private set

    override fun find(name: String): Command? { //TODO оптмизировать
        val result = commands.find { it.name == name}
        if(result == null) {
            for (command in commands)
                for (alias in command.aliases)
                    if(alias == name)
                        return command
            return null
        }
        return result
    }

    override fun addCommand(builder: CommandBuilder.() -> Command) {
        val command = builder(CommandBuilder())
        commands = arrayOf(*commands, command)
    }

    public override fun addModule(module: ModuleBase<GCommandContext>) : GCommandProvider {
        modules = arrayOf(*modules, module)
        super.addModule(module)
        return this
    }
}