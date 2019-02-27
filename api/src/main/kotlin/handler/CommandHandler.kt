package handler

import command.CommandInfo
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.util.Loggers
import reactor.util.function.component1
import reactor.util.function.component2
import util.zipWith
import javax.naming.NoPermissionException
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser
    private val logger = Loggers.getLogger(CommandHandler::class.java)

    private fun checkPermissions(member: Mono<Member>, neededPerms: PermissionSet) =
            member.flatMap { it.basePermissions }
                    .map {
                        val copy = neededPerms.asEnumSet().clone()
                        copy.removeAll(it)
                        if (copy.isEmpty()) return@map true
                        throw NoPermissionException("Недостаточно привелегий")
                    }

    protected fun execute(command: CommandInfo, context: ICommandContext): Disposable =
            checkPermissions(context.member, command.permissions)
                    .filter { it }
                    .thenReturn(command.modulePointer.setContext(context))
                    .doOnNext { parser = Parser(context) }
                    .then(parseParameters(command)).zipWith(command)
                    .doOnNext { (params, command) ->
                        command.functionPointer.callBy(params)
                    }.subscribe({}, { err ->
                        context.message.channel.subscribe { it.createMessage(err.cause?.message ?: err.message ?: "Ошибка").subscribe() }
                        logger.error(err.localizedMessage, err)
                    })

    private fun parseParameters(command: CommandInfo): Mono<Map<KParameter, Any?>> =
            command.parameters.toFlux()
                    .collectMap({ it }, {
                        val type = it.type.classifier as KClass<*>
                        val index = it.index - 1

                        when {
                            it.isOptional -> parser.parseOptional(index, type).block()
                            type == command.modulePointer::class -> command.modulePointer
                            else -> parser.parse(index, type).block()
                        }
                    }).map { it.filterValues { value -> value != null } }
}