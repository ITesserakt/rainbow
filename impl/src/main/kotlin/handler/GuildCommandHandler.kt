package handler

import command.CommandInfo
import command.GuildCommandProvider
import command.processors.PermissionsProcessor
import command.processors.RequiredOwnerProcessor
import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import util.Loggers
import util.NoPermissionsException
import util.await
import util.isNotPresent

class GuildCommandHandler : CommandHandler(), PermissionsProcessor, RequiredOwnerProcessor {
    private val logger = Loggers.getLogger<GuildCommandHandler>()

    override suspend fun processOwner(command: CommandInfo, context: ICommandContext) {
        context as GuildCommandContext
        if (command.isRequiringOwner && context.author.id != context.guild.await().ownerId)
            throw NoPermissionsException("Только владелец гильдии может запустить это")
    }

    override suspend fun processPerms(command: CommandInfo, context: ICommandContext) {
        val authorPerms = context.message.authorAsMember
            .flatMap { it.basePermissions }.await()

        val copy = command.permissions.asEnumSet().clone()
        copy.removeAll(authorPerms)
        if (copy.isNotEmpty()) {
            throw NoPermissionsException()
        }
    }

    override fun handle(event: MessageCreateEvent) = GlobalScope.launch {
        if (event.guildId.isNotPresent) return@launch
        if (event.message.content.isNotPresent) return@launch
        if (event.member.isNotPresent) return@launch

        val content = event.message.content.get().split(' ')
        if (!content[0].startsWith('!')) return@launch

        val args = content.drop(1) //выбрасываем имя команды
        val context = GuildCommandContext(event, args)
        val command = GuildCommandProvider.find(content[0].drop(1)) ?: return@launch

        runCatching {
            processPerms(command, context)
            processOwner(command, context)
            executeAsync(command, context)
        }.onFailure {
            context.channel.await().createMessage(getError(it)).subscribe()
            logger.error(" ", it)
        }
    }
}
