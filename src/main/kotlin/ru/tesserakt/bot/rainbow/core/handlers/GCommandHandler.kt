package ru.tesserakt.bot.rainbow.core.handlers

import discord4j.core.event.domain.message.MessageCreateEvent
import ru.tesserakt.bot.rainbow.core.commands.GCommandProvider
import ru.tesserakt.bot.rainbow.core.context.GCommandContext

class GCommandHandler : Handler<MessageCreateEvent>() {
    override fun handle(event: MessageCreateEvent) {
        if (!event.guildId.isPresent) return
        if (!event.message.content.isPresent) return

        val content = event.message.content.get().split(' ')
        if (content[0].isEmpty() || content[0][0] != '!') return

        val args = content.drop(1)

        val context = GCommandContext(event, args)

        val command = GCommandProvider.find(content[0].drop(1)) ?: return

        runCatching {
            execute(command, context)
        }.onFailure { ex -> context.message.channel
                .subscribe { it.createMessage("Ошибка: ${ex.localizedMessage}").subscribe() }
        }
    }
}