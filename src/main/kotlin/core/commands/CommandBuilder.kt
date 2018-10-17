package core.commands

data class CommandBuilder (private val name : String, @Transient private val action : (CommandContext) -> Unit) {
    private val command = object : Command {
        override fun toString(): String =
            StringBuilder(this.name)
                    .append(" (")
                    .append(this.parameters.map {
                        if(!it.isOptional) "${it.name} : ${it.type!!.simpleName}"   //some : Int
                        else "<${it.name} : ${it.type!!.simpleName}>"}              //<some : Int>
                            .joinToString { it })
                    .append(")")
                    .toString()

        override fun execute(context: CommandContext) {
            action(context)
        }

        override var aliases = listOf("")
        override var summary = "Описание отстутствует"
        override var name = this@CommandBuilder.name
        override var parameters: Array<out ParamInfo> = arrayOf()
    }

    fun addAliases(vararg args: String) : CommandBuilder {
        command.aliases = args.toList()
        return this
    }

    fun addSummary(summary: String) : CommandBuilder {
        command.summary = summary
        return this
    }

    fun defineParameters(vararg args : ParamInfo): CommandBuilder {
        command.parameters = args
        return this
    }

    fun build(): Command {
        if(command.summary == "Описание отстутствует")
            println("Отсутствует описание для функции '${command.name}'")

        return command
    }
}