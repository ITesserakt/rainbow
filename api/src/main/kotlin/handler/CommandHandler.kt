package handler

import command.CommandInfo
import command.Continuous
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.Mono
import reactor.core.publisher.toFlux
import reactor.util.function.component1
import reactor.util.function.component2
import util.hasAnnotation
import util.zipWith
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser

    protected fun execute(command: CommandInfo, context: ICommandContext): Mono<Any?> =
        Mono.fromCallable {
            command.modulePointer.setContext(context)
            parser = Parser(context)
        }
            .flatMap { context.author }
            .map { it.id.asLong() }
            .filter { !command.isRequiringDeveloper || it == 316249690092077065 }
            .flatMap { parseParameters(command) }
            .zipWith(command)
            .map { (params, command) ->
                command.functionPointer.callBy(params)
            }

    protected tailrec fun getError(error: Throwable?): String =
        if (error?.message.isNullOrEmpty())
            getError(error?.cause)
        else error?.message ?: "неизвестно"

    private fun parseParameters(command: CommandInfo): Mono<Map<KParameter, Any?>> =
        command.parameters.toFlux()
            .collectMap({ it }, {
                val type = it.type.classifier as KClass<*>
                val index = it.index - 1
                val isContinuous = it.hasAnnotation<Continuous>()

                when {
                    it.isOptional -> parser.parseOptional(index, type, isContinuous).block()
                    type == command.modulePointer::class -> command.modulePointer
                    else -> parser.parse(index, type, isContinuous).block()
                }
            }).map { it.filterValues { value -> value != null } }
}