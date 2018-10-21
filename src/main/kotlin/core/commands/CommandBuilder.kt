package core.commands

/**
 * Строитель для команды
 */
class CommandBuilder {
    private val command: Command

    init {
        command = object : Command {
            override fun toString(): String =
                    StringBuilder(this.name)
                            .append(" (")
                            .append(this.parameters.map {
                                if (!it.isOptional) "${it.name} : ${it.type?.simpleName}"   //some : Int
                                else "<${it.name} : ${it.type?.simpleName}>"                //<some : Int>
                            }
                                    .joinToString { it })
                            .append(")")
                            .toString()

            override var action: (CommandContext) -> Unit = {}
            override var aliases: Array<String> = arrayOf()
            override var summary = "Описание отстутствует"
            override var name = ""
            override var parameters: Array<out ParamInfo> = arrayOf()
        }
    }

    var action: (CommandContext) -> Unit = {}
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

    var name: String = ""
        set(value) {
            command.name = value
        }

    /**
     * Заверщает построение команды
     */
    fun build(): Command {
        if (command.name == "")
            throw NullPointerException("Отсутствует имя команды")
        if (action == {})
            throw NullPointerException("Отсутствует действие, выполняемое командой")
        if (command.summary == "Описание отстутствует" || command.summary == "")
            println("Отсутствует описание для функции '${command.name}'")

        return command
    }
}