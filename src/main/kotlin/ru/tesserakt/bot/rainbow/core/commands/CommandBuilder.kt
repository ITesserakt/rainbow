package ru.tesserakt.bot.rainbow.core.commands

import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import reactor.util.Logger
import reactor.util.Loggers
import ru.tesserakt.bot.rainbow.core.ModuleBase
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter

class CommandBuilder {
    private val logger: Logger = Loggers.getLogger(CommandBuilder::class.java)

    private lateinit var parentFunc: KFunction<*>
    private var aliases: Array<out String> = arrayOf()
    private var description: String = ""
    private var parameters: Array<out KParameter> = arrayOf()
    private var permissions: PermissionSet = PermissionSet.none()
    private var name: String = ""
    private lateinit var parentModule: ModuleBase<*>

    internal fun setName(name: String) {
        this.name = name
    }

    internal fun setDescription(description: String) {
        this.description = description
    }

    internal fun setParentFunc(parentFunc: KFunction<*>) {
        this.parentFunc = parentFunc
    }

    internal fun setAliases(vararg aliases: String) {
        this.aliases = aliases
    }

    internal fun setParams(vararg parameters: KParameter) {
        this.parameters = parameters
    }

    internal fun setParentModule(module: ModuleBase<*>) {
        this.parentModule = module
    }

    internal fun setPermissions(vararg permissions: Permission) {
        this.permissions = PermissionSet.of(*permissions)
    }

    internal fun build(): Command {
        if (name.isBlank()) throw IllegalArgumentException("Не задано имя команды")
        if (!this::parentFunc.isInitialized) throw IllegalArgumentException("Команда ничего не выполняет")
        if (!this::parentModule.isInitialized) throw IllegalArgumentException("Указатель на модуль отсутствует!")
        if (description.isBlank()) logger.warn("Отсутствует описание для функции '$name'")

        return Command(name, description, parentFunc, parentModule, parameters, aliases, permissions)
    }
}