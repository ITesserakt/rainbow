package handler

import command.CommandInfo
import command.GuildCommandProvider
import context.GuildCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import util.Loggers
import util.NoPermissionsException
import util.await
import util.isNotPresent

class GuildCommandHandler : CommandHandler() {
    private val logger = Loggers.getLogger<GuildCommandHandler>()

    override fun handle(event: MessageCreateEvent) = GlobalScope.launch {
        if (isNotRightEvent(event)) return@launch

        val content = event.message.content.orElse(" ").split(' ')
        if (!content[0].startsWith('!')) return@launch

        val args = content.drop(1) //выбрасываем имя команды
        val context = GuildCommandContext(event, args)
        val command = GuildCommandProvider.find(content[0].drop(1)) ?: return@launch

        try {
            checkForPermissions(context, command)
        } catch (ex: NoPermissionsException) {
            throw ex
        }

        if (command.isRequiringOwner && context.author.id != context.guild.await().ownerId)
            return@launch

        runCatching {
            executeAsync(command, context).await()
        }.onFailure { err ->
            context.channel.await().createMessage("Ошибка: ${getError(err)}").subscribe()
            logger.error(err.localizedMessage, err)
        }
    }

    private suspend fun checkForPermissions(context: GuildCommandContext, command: CommandInfo) {
        val authorPerms = context.message.authorAsMember
            .flatMap { it.basePermissions }.await()
        val copy = command.permissions.asEnumSet().clone()
        copy.removeAll(authorPerms)
        if (copy.isNotEmpty())
            throw NoPermissionsException()
    }

    private fun isNotRightEvent(event: MessageCreateEvent): Boolean = when {
        event.guildId.isNotPresent &&
                event.message.content.isNotPresent &&
                event.member.isNotPresent -> true
        else -> false
    }
}
