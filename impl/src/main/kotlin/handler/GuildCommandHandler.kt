package handler

import command.GuildCommandProvider
import context.GuildCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import util.Loggers
import util.isNotPresent

class GuildCommandHandler : CommandHandler() {
    private val logger = Loggers.getLogger<GuildCommandHandler>()

    override suspend fun handle(event: MessageCreateEvent) {
        if (event.guildId.isNotPresent) return
        if (event.message.content.isNotPresent) return
        if (event.member.isNotPresent) return

        //TODO !cmd name ... тоже должно работать
        val content = event.message.content.get()
            .split(Regex("""\s+(?=([^"]*"[^"]*")*[^"]*$)"""))
            .map { it.replace("\"", "") }
            .filter { it.isNotEmpty() }

        if (!content[0].startsWith('!')) return

        val args = content.drop(1) //выбрасываем имя команды
        val context = GuildCommandContext(event, args)
        val command = GuildCommandProvider.find(content[0].drop(1)) ?: return

        runCatching {
            executeAsync(command, context)
        }.onFailure {
            context.channel.await().createMessage(it.localizedMessage).subscribe()
            logger.error(" ", it)
        }
    }
}
