package core.commands

interface Command {
    var name : String
    var summary : String
    var aliases : Array<String>
    var parameters : Array<out ParamInfo>

    fun execute(context: CommandContext)

    override fun toString(): String

}