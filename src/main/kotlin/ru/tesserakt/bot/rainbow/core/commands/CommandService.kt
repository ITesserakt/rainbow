package ru.tesserakt.bot.rainbow.core.commands

import ru.tesserakt.bot.rainbow.core.IService
import ru.tesserakt.bot.rainbow.core.ModuleBase
import java.util.*
import kotlin.collections.ArrayList

/**
 * Предоставляет методы для упрвления командами
 */
object CommandService : IService<CommandContext> {
    private val commandsMap = HashMap<String, CommandInfo>()
    override val commandsList: Collection<CommandInfo> = commandsMap.values
    internal val modules = ArrayList<ModuleBase<*>>()

    override fun addCommand(init: CommandBuilder.() -> CommandInfo) {
        val command = CommandBuilder().init()
        commandsMap[command.name] = command
    }

    override fun addModule(module : ModuleBase<CommandContext>) : IService<CommandContext> {
        modules.add(module)
        return super.addModule(module)
    }

    /**
     * Возвращает команду
     */
    override fun getCommandByName(name: String) = commandsMap[name]

    override fun getCommandByAlias(param: String): CommandInfo? {
        for (command in commandsList)
            for (alias in command.aliases)
                if (alias == param)
                    return command
        return null
    }
}