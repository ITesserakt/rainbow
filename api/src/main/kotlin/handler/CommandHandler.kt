package handler

import command.CommandInfo
import command.Continuous
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.core.publisher.toMono
import reactor.util.function.component1
import reactor.util.function.component2
import util.Loggers
import util.zipWith
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser
    private val logger = Loggers.getLogger<CommandHandler>()

    protected fun execute(command: CommandInfo, context: ICommandContext): Mono<Any?> =
            command.modulePointer.setContext(context).toMono()
                    .doOnNext { parser = Parser(context) }
                    .then(parseParameters(command)).zipWith(command)
                    .map { (params, command) ->
                        command.functionPointer.callBy(params)
                    }.doOnError { err ->
                        context.message.channel
                                .flatMap { it.createMessage("Ошибка: ${err.localizedMessage}") }
                                .subscribe {
                                    logger.error(it.content.get(), err)
                                }
                    }

    private fun parseParameters(command: CommandInfo): Mono<Map<KParameter, Any?>> =
            command.parameters.toFlux()
                    .collectMap({ it }, {
                        val type = it.type.classifier as KClass<*>
                        val index = it.index - 1
                        val isContinuous = it.findAnnotation<Continuous>() != null

                        when {
                            it.isOptional -> parser.parseOptional(index, type, isContinuous).block()
                            type == command.modulePointer::class -> command.modulePointer
                            else -> parser.parse(index, type, isContinuous).block()
                        }
                    }).map { it.filterValues { value -> value != null } }
}