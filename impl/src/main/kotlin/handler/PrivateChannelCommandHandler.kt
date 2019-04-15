package handler

import command.CommandProvider
import command.PrivateChannelCommandProvider
import context.ICommandContext
import context.PrivateChannelCommandContext
import discord4j.core.event.domain.message.MessageCreateEvent
import util.isNotPresent

class PrivateChannelCommandHandler : CommandHandler() {
    override val prefix = ""

    override fun defineContracts(event: MessageCreateEvent): Boolean =
        event.guildId.isNotPresent
                && event.member.isNotPresent
                && event.message.author.isPresent
                && event.message.content.isPresent

    override fun setContext(event: MessageCreateEvent, args: List<String>): ICommandContext =
        PrivateChannelCommandContext(event, args)

    override val provider: CommandProvider<out ICommandContext> = PrivateChannelCommandProvider
}
