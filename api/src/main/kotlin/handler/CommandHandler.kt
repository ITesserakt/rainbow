package handler

import command.CommandInfo
import command.Continuous
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import util.hasAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.callSuspendBy

abstract class CommandHandler : Handler<MessageCreateEvent>() {
    private lateinit var parser: Parser

    protected fun executeAsync(command: CommandInfo, context: ICommandContext) = GlobalScope.async {
        command.modulePointer.setContext(context)
        parser = Parser(context)
        val authorId = context.author.id
        if (command.isRequiringDeveloper && authorId.asLong() != 316249690092077065) return@async null

        val ps = parseParameters(command)
        command.functionPointer.callSuspendBy(ps)
    }

    protected tailrec fun getError(error: Throwable?): String =
        if (error?.message.isNullOrEmpty())
            getError(error?.cause)
        else error?.message ?: "неизвестно"

    private suspend fun parseParameters(command: CommandInfo): MutableMap<KParameter, Any?> {
        val params = mutableMapOf<KParameter, Any?>()

        for (p in command.parameters) {
            val isContinuous = p.hasAnnotation<Continuous>()
            val type = p.type.classifier as KClass<*>

            if (type == command.modulePointer::class) {
                params[p] = command.modulePointer
                continue
            }

            params[p] = if (p.isOptional)
                parser.parseOptional(p.index - 1, type, isContinuous) ?: continue
            else
                parser.parse(p.index - 1, type, isContinuous)
        }

        return params
    }
}