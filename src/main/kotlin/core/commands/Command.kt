package core.commands

interface Command {
    var name : String
    var summary : String
    var aliases : Array<String>
    var parameters : Array<out ParamInfo>
    var action : (CommandContext) -> Unit

    override fun toString(): String

}