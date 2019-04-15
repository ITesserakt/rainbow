package handler

import command.CommandInfo
import command.CommandProvider
import command.Continuous
import command.limiters.DeveloperLimiter
import command.limiters.OwnerLimiter
import command.limiters.PermissionsLimiter
import context.GuildCommandContext
import context.ICommandContext
import createMessageAsync
import discord4j.core.event.domain.message.MessageCreateEvent
import util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy

/**
 * A handler, which can run command
 */
abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser
    private val logger = Loggers.getLogger<CommandHandler>()

    override suspend fun handle(event: MessageCreateEvent) {
        if (!defineContracts(event)) return

        val content = event.message.content.get()
            .split(Regex("""\s+(?=([^"]*"[^"]*")*[^"]*$)"""))
            .map { it.replace("\"", "") }
            .filter { it.isNotEmpty() }

        if (!content[0].startsWith(prefix)) return

        val args = content.drop(1)
        val context = setContext(event, args)
        val command = provider.find(content[0].drop(prefix.length)) ?: return
        parser = Parser(context)

        runCatching {
            command.modulePointer.setContext(context)
            if (!resolveLimiters(command, context)) throw NoPermissionsException()
            val params = parseParameters(command)
            command.functionPointer.callSuspendBy(params)
        }.onFailure {
            val error = CommandException(it)
            context.channel.await()
                .createMessageAsync("Something went wrong running command ${command.name} \n|| ${error.localizedMessage} ||")
            logger.error(" ", error)
        }
    }

    protected abstract fun defineContracts(event: MessageCreateEvent): Boolean
    protected abstract fun setContext(event: MessageCreateEvent, args: List<String>): ICommandContext
    protected abstract val provider: CommandProvider<out ICommandContext>
    protected abstract val prefix: String

    private suspend fun parseParameters(command: CommandInfo): Map<KParameter, Any?> =
        command.parameters.associateWith {
        val isContinuous = it.hasAnnotation<Continuous>()
            val type = it.type.classifier as KClass<out Any>

        return@associateWith when {
            type == command.modulePointer::class -> command.modulePointer
            it.isOptional -> parser.parseOptional(it.index - 1, type, isContinuous)
            else -> parser.parse(it.index - 1, type, isContinuous)
        }
        }.filterValues { it != null }

    private suspend fun resolveLimiters(commandInfo: CommandInfo, context: ICommandContext): Boolean {
        if (commandInfo.isRequiringDeveloper && !DeveloperLimiter().checkAccess(
                context, listOf(316249690092077065.toSnowflake())
            )
        ) return false
        if (commandInfo.isRequiringOwner && context is GuildCommandContext && !OwnerLimiter().checkAccess(
                context, listOf(context.guild.await().ownerId)
            )
        ) return false
        if (context is GuildCommandContext && !PermissionsLimiter().checkAccess(
                context, commandInfo.permissions.toList()
            )
        ) return false
        return true
    }
}