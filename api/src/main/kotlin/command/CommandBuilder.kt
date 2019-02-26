package command

import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import reactor.util.Loggers
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

internal class CommandBuilder private constructor() :
        CommandBuilderSteps.BuildStep,
        CommandBuilderSteps.FuncStep,
        CommandBuilderSteps.ModuleStep,
        CommandBuilderSteps.NameStep {
    private val logger = Loggers.getLogger(CommandBuilder::class.java)

    companion object {
        fun createNew(): CommandBuilderSteps.NameStep = CommandBuilder()
    }

    private lateinit var name: String
    override fun setName(name: String): CommandBuilderSteps.FuncStep = apply {
        require(name.isNotBlank()) { "Не задано имя команды" }

        this.name = name
    }

    private lateinit var funcPointer: KFunction<*>
    override fun <T> setFuncPointer(functionPointer: KFunction<T>): CommandBuilderSteps.ModuleStep = apply {
        require(!functionPointer.isAbstract
                && !functionPointer.isSuspend
                && !functionPointer.isOperator
                && !functionPointer.isExternal
                && !functionPointer.isInfix) {
            "Неподдерживаемый тип функции"
        }

        funcPointer = functionPointer
    }

    private lateinit var modulePointer: ModuleBase<*>
    override fun <T : ICommandContext> setModulePointer(modulePointer: ModuleBase<T>): CommandBuilderSteps.BuildStep =
            apply {
                this.modulePointer = modulePointer
            }

    private var description: String = ""
    override fun setDescription(desc: String) : CommandBuilderSteps.BuildStep = apply {
        description = desc
    }

    private var parameters: Array<KParameter> = emptyArray()
    override fun setParams(params: Array<KParameter>) : CommandBuilderSteps.BuildStep = apply {
        parameters = params
    }

    private var perms: PermissionSet = PermissionSet.none()
    override fun setPermissions(permissions: PermissionSet) : CommandBuilderSteps.BuildStep = apply {
        perms = permissions
    }

    override fun build(): CommandInfo {
        if (description.isBlank())
            logger.warn("Отсутствует описание для функции '$name'")

        return CommandInfo(name, description, funcPointer, modulePointer, parameters, perms)
    }
}

internal class CommandBuilderSteps {
    internal interface NameStep {
        fun setName(name: String): FuncStep
    }

    internal interface FuncStep {
        fun <T> setFuncPointer(functionPointer: KFunction<T>): ModuleStep
    }

    internal interface ModuleStep {
        fun <T : ICommandContext> setModulePointer(modulePointer: ModuleBase<T>): BuildStep
    }

    internal interface BuildStep {
        fun build(): CommandInfo
        fun setDescription(desc: String): BuildStep
        fun setParams(params: Array<KParameter>): BuildStep
        fun setPermissions(permissions: PermissionSet): BuildStep
    }
}
