package handler

import command.GuildCommandProvider
import context.GuildCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.toMono
import reactor.util.function.Tuples
import reactor.util.function.component1
import reactor.util.function.component2
import util.toOptional

class GuildCommandHandler : CommandHandler() {
    override fun handle(event: MessageCreateEvent) {
        event.toMono()
                .filter { it.guildId.isPresent }
                .filter { it.message.content.isPresent }
                .filter { it.member.isPresent }
                .map { it.message.content.get().split(' ') }
                .filter { it[0].isNotEmpty() && it[0].startsWith("!test_") }
                .map { content ->
                    val args = content.drop(1) //выбрасываем имя команды
                    val context = GuildCommandContext(event, args)
                    val command = GuildCommandProvider.find(content[0].drop(6)).toOptional()
                    Tuples.of(context, command)
                }
                .filter { it.t2.isPresent }
                .map { Tuples.of(it.t1, it.t2.get()) }
                .doOnNext { (context, command) ->
                    execute(command, context)
                }
                .doOnError { err ->
                    event.message.channel.subscribe {
                        it.createMessage("Ошибка: ${err.localizedMessage}").subscribe()
                    }
                }.subscribe()
    }
}
