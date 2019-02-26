package command

import discord4j.core.`object`.util.PermissionSet
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class CommandInfo internal constructor(
        val name: String,
        val description: String,
        internal val functionPointer: KFunction<*>,
        internal val modulePointer: ModuleBase<*>,
        internal val parameters: Array<KParameter>,
        internal val permissions: PermissionSet
) {
    private fun stringifyParams() = parameters
            .drop(1)
            .map {
                val pattern = "${it.name} : ${(it.type.jvmErasure).simpleName}"

                if (!it.isOptional) "<$pattern>"
                else "[$pattern]"
            }.joinToString { it }

    override fun toString(): String = StringBuilder(name)
            .append(" (")
            .append(stringifyParams())
            .append(')')
            .toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CommandInfo

        if (name != other.name) return false
        if (description != other.description) return false
        if (functionPointer != other.functionPointer) return false
        if (modulePointer != other.modulePointer) return false
        if (!parameters.contentEquals(other.parameters)) return false
        if (permissions != other.permissions) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + functionPointer.hashCode()
        result = 31 * result + modulePointer.hashCode()
        result = 31 * result + parameters.contentHashCode()
        result = 31 * result + permissions.hashCode()
        return result
    }
}