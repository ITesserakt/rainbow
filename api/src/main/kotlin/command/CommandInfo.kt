package command

import discord4j.core.`object`.util.PermissionSet
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.jvmErasure

data class CommandInfo(
        val name: String,
        val description: String,
        internal val functionPointer: KFunction<*>,
        internal val modulePointer: ModuleBase<*>,
        internal val permissions: PermissionSet
) {
    internal val parameters: List<KParameter> = functionPointer.parameters

    init {
        require(name.isNotEmpty() && ' ' !in name) { "Имя не должно содержать пробелов или быть пустым" }
        require(!functionPointer.isExternal
                && !functionPointer.isInfix
                && !functionPointer.isInline
                && !functionPointer.isOperator
                && !functionPointer.isAbstract
                && !functionPointer.isSuspend) {
            "Неподдерживаемый тип функции"
        }
    }

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
}