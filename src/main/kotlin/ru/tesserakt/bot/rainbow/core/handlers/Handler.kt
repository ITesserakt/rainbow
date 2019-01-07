package ru.tesserakt.bot.rainbow.core.handlers

import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.event.domain.Event
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import ru.tesserakt.bot.rainbow.core.commands.Command
import ru.tesserakt.bot.rainbow.core.commands.Remainder
import ru.tesserakt.bot.rainbow.core.context.ICommandContext
import ru.tesserakt.bot.rainbow.core.types.ResolverService
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

abstract class Handler <T : Event> {
    abstract fun handle(event: T)

    protected fun checkPermissions(neededPerms: PermissionSet, user: Mono<Member>): Mono<Boolean> =
            user.flatMap { it.basePermissions }
                    .map {
                        val copy = neededPerms.asEnumSet().clone()
                        copy.removeAll(it)
                        copy.isEmpty()
                    }.toMono()

    protected fun execute(command: Command, context: ICommandContext) {
        command.parentModule.setContextInternal(context)

        val params = parseParameters(command, context)
        command.parentFunc.callBy(params)
    }

    private fun parseParameters(command: Command, context: ICommandContext): MutableMap<KParameter, Any> {
        val params = mutableMapOf<KParameter, Any>()

        for (param in command.parameters) {
            val remainder = param.findAnnotation<Remainder>() != null
            val type = param.type.classifier as KClass<*>

            if (type == command.parentModule::class) {
                params[param] = command.parentModule
                continue
            }

            if (param.isOptional)
                params[param] = ResolverService.parseOptional(type, context, param.index - 1, remainder) ?: continue
            else
                params[param] = ResolverService.parse(type, context, param.index - 1, remainder)
        }
        return params
    }
}