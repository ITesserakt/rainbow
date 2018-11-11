package core.commands

import core.ICommandContext
import sx.blah.discord.handle.obj.Permissions

class Command {
    var name : String = ""
    var summary : String = "Описание отстутствует"
    var aliases : Array<String> = arrayOf()
    var parameters : Array<out ParamInfo> = arrayOf()
    var action : (ICommandContext) -> Unit = {}
    var restrictions : Array<out Permissions> = arrayOf()

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

}