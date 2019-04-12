package handler

import command.CommandInfo
import command.Continuous
import command.limiters.DeveloperLimiter
import command.limiters.OwnerLimiter
import command.limiters.PermissionsLimiter
import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import util.CommandException
import util.NoPermissionsException
import util.hasAnnotation
import util.toSnowflake
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy

/**
 * A handler, which can run command using [executeAsync]
 */
abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser

    protected suspend fun executeAsync(command: CommandInfo, context: ICommandContext): Any? {
        command.modulePointer.setContext(context)
        parser = Parser(context)
        if (!resolveLimiters(command, context)) throw NoPermissionsException()

        runCatching { command.functionPointer.callSuspendBy(parseParameters(command)) }
            .onSuccess { return it }
            .onFailure { throw CommandException(it) }

        return null
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun parseParameters(command: CommandInfo): Map<KParameter, Any> = command.parameters.associateWith {
        val isContinuous = it.hasAnnotation<Continuous>()
        val type = it.type.classifier as KClass<*>

        return@associateWith when {
            type == command.modulePointer::class -> command.modulePointer
            it.isOptional -> parser.parseOptional(it.index - 1, type, isContinuous)
            else -> parser.parse(it.index - 1, type, isContinuous)
        }
    }.filterValues { it != null } as Map<KParameter, Any>

    private suspend fun resolveLimiters(commandInfo: CommandInfo, context: ICommandContext): Boolean {
        if (commandInfo.isRequiringDeveloper && !DeveloperLimiter().checkAccess(
                context to listOf(316249690092077065.toSnowflake())
            )
        ) return false
        if (commandInfo.isRequiringOwner && context is GuildCommandContext && !OwnerLimiter().checkAccess(
                context to listOf(context.guild.await().ownerId)
            )
        ) return false
        if (context is GuildCommandContext && !PermissionsLimiter().checkAccess(
                context to commandInfo.permissions.toList()
            )
        ) return false
        return true
    }
}