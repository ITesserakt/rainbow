package handler

import command.CommandInfo
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import reactor.util.function.component1
import reactor.util.function.component2
import util.Loggers
import util.zipWith
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser
    private val logger = Loggers.getLogger<CommandHandler>()

    protected fun execute(command: CommandInfo, context: ICommandContext): Disposable =
            command.modulePointer.setContext(context).toMono()
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