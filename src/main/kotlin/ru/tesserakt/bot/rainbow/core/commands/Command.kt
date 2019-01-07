package ru.tesserakt.bot.rainbow.core.commands

import discord4j.core.`object`.util.PermissionSet
import ru.tesserakt.bot.rainbow.core.ModuleBase
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class Command internal constructor (
        val name : String,
        val description : String,
        val parentFunc : KFunction<*>,
        val parentModule : ModuleBase<*>,
        val parameters : Array<out KParameter>,
        val aliases : Array<out String>,
        val permissions: PermissionSet = PermissionSet.none()
        ) {
    override fun toString(): String = StringBuilder(name)
            .append(" (")
            .append( parameters
                    .drop(1)
                    .map {
                        if (!it.isOptional) "${it.name} : ${it.type.jvmErasure.simpleName}"
                        else "<${it.name} : ${it.type.jvmErasure.simpleName}>"
                    }
                    .joinToString { item -> item })
            .append(") ")
            .toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Command

        if (name != other.name) return false
        if (description != other.description) return false
        if (parentFunc != other.parentFunc) return false
        if (parentModule != other.parentModule) return false
        if (!parameters.contentEquals(other.parameters)) return false
        if (!aliases.contentEquals(other.aliases)) return false
        if (permissions != other.permissions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + parentFunc.hashCode()
        result = 31 * result + parentModule.hashCode()
        result = 31 * result + parameters.contentHashCode()
        result = 31 * result + aliases.contentHashCode()
        result = 31 * result + permissions.hashCode()
        return result
    }
}
