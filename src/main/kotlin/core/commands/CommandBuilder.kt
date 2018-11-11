package core.commands

import core.ICommandContext
import org.slf4j.LoggerFactory
import sx.blah.discord.handle.obj.Permissions

/**
 * Строитель для команды
 */
class CommandBuilder {
    private val command: Command = Command()

    var action: (ICommandContext) -> Unit = {}
        set(value) {
            command.action = value
        }

    var aliases: Array<String> = arrayOf()
        set(value) {
            command.aliases = value
        }

    var summary: String = ""
        set(value) {
            command.summary = value
        }

    fun parameters(vararg params: ParamBuilder.() -> ParamInfo) {
        command.parameters = params.map { it.invoke(ParamBuilder()) }.toTypedArray()
    }

    fun restrictions(vararg perms : Permissions) {
        command.restrictions = perms
    }

    fun restrictions(permNumber : Int) {
        command.restrictions = Permissions.getAllowedPermissionsForNumber(permNumber).toTypedArray()
    }

    var name: String = ""
        set(value) {
            command.name = value
        }

    /**
     * Заверщает построение команды
     */
    fun build(): Command {
        val logger = LoggerFactory.getLogger(CommandBuilder::class.java)

        if (command.name == "")
            throw NullPointerException("Отсутствует имя команды")
        if (action == {})
            throw NullPointerException("Отсутствует действие, выполняемое командой")
        if (command.summary == "Описание отстутствует" || command.summary == "")
            logger.warn("Отсутствует описание для функции '${command.name}'")
        if(command.restrictions.isEmpty())
            logger.info("Каждый пользователь может использовать '${command.name}'")

        return command
    }
}