package ru.tesserakt.bot.rainbow.core.commands

import ru.tesserakt.bot.rainbow.core.ModuleBase
import sx.blah.discord.handle.obj.Permissions
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class CommandInfo(val name: String, val summary: String,
                       val aliases: Array<out String> = arrayOf(), val parameters: Array<out KParameter>,
                       val restrictions: Array<out Permissions> = arrayOf(), val funObj: KFunction<*>,
                       val parentModule: ModuleBase<*>) {

    override fun toString(): String =
            StringBuilder(this.name)
                    .append(" (")
                    .append(this.parameters.drop(1).map {
                        if (!it.isOptional) "${it.name} : ${it.type.jvmErasure.simpleName}"   //some : Int
                        else "<${it.name} : ${it.type.jvmErasure.simpleName}>"                //<some : Int>
                    }
                            .joinToString { it })
                    .append(")")
                    .toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandInfo

        if (name != other.name) return false
        if (summary != other.summary) return false
        if (!aliases.contentEquals(other.aliases)) return false
        if (!parameters.contentEquals(other.parameters)) return false
        if (!restrictions.contentEquals(other.restrictions)) return false
        if (funObj != other.funObj) return false
        if (parentModule != other.parentModule) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + summary.hashCode()
        result = 31 * result + aliases.contentHashCode()
        result = 31 * result + parameters.contentHashCode()
        result = 31 * result + restrictions.contentHashCode()
        result = 31 * result + funObj.hashCode()
        result = 31 * result + parentModule.hashCode()
        return result
    }

}