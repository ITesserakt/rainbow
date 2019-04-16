package command

import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class CommandInfo(
    val name: String,
    val description: String,
    internal val functionPointer: KFunction<*>,
    internal val modulePointer: ModuleBase<ICommandContext>,
    internal val aliases: List<String>,
    internal val permissions: PermissionSet,
    internal val isHidden: Boolean,
    internal val isRequiringDeveloper: Boolean,
    internal val isRequiringOwner: Boolean
) {
    internal val parameters: List<KParameter> = functionPointer.parameters

    private fun stringifyParams() = parameters
            .drop(1)
            .map {
                val pattern = "${it.name} : ${it.type.jvmErasure.simpleName}"

                if (!it.isOptional) "<$pattern>"
                else "[$pattern]"
            }.joinToString { it }

    override fun toString(): String = StringBuilder(name)
            .append(" (")
            .append(stringifyParams())
            .append(')')
            .toString()
}