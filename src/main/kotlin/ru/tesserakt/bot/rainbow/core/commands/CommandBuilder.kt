package ru.tesserakt.bot.rainbow.core.commands

import org.slf4j.LoggerFactory
import ru.tesserakt.bot.rainbow.core.ModuleBase
import sx.blah.discord.handle.obj.Permissions
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

/**
 * Строитель для команды
 */
class CommandBuilder {
    internal var funObject : KFunction<*>? = null
    internal var aliases: Array<out String> = arrayOf()
    internal var summary: String = ""
    internal var parameters : Array<out KParameter> = arrayOf()
    internal var restrictions : Array<out Permissions> = arrayOf()
    internal var name: String = ""
    internal var parentModule: ModuleBase<*>? = null

    /**
     * Заверщает построение команды
     */
    internal fun build(): CommandInfo {
        val logger = LoggerFactory.getLogger(CommandBuilder::class.java)

        if (name.isBlank())
            throw NullPointerException("Отсутствует имя команды")
        if (funObject == null)
            throw NullPointerException("Отсутствует действие, выполняемое командой")
        if (summary.isBlank())
            logger.warn("Отсутствует описание для функции '$name'")
        if(restrictions.isEmpty())
            logger.info("Каждый пользователь может использовать '$name'")
        if(parentModule == null)
            throw NullPointerException("Указатель на модуль отсутствует")

        return CommandInfo(name, summary, aliases, parameters, restrictions, funObject!!, parentModule!!)
    }
}