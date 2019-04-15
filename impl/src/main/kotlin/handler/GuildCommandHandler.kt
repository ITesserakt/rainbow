package handler

import command.CommandProvider
import command.GuildCommandProvider
import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.event.domain.message.MessageCreateEvent

class GuildCommandHandler : CommandHandler() {
    override val prefix = "!"

    override fun defineContracts(event: MessageCreateEvent): Boolean =
        event.guildId.isPresent
                && event.member.isPresent
                && event.message.content.isPresent

    override fun setContext(event: MessageCreateEvent, args: List<String>): ICommandContext =
        GuildCommandContext(event, args)

    override val provider: CommandProvider<out ICommandContext> = GuildCommandProvider
}
