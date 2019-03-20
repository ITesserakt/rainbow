package handler

import command.GuildCommandProvider
import context.GuildCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import reactor.core.publisher.toMono
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
                    context to command
                }.filter { (_, command) -> command.isPresent }
                .map { it.first to it.second.get() }
                .flatMap { (context, command) ->
                    execute(command, context)
                }.subscribe({},
                        { err ->
                            event.message.channel.subscribe {
                                it.createMessage("Ошибка: ${err.localizedMessage}").subscribe()
                            }
                        })
    }
}
