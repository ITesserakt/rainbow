package ru.tesserakt.bot.rainbow.modules

import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.Prefix
import ru.tesserakt.bot.rainbow.core.commands.Command
import ru.tesserakt.bot.rainbow.core.commands.CommandContext
import ru.tesserakt.bot.rainbow.core.commands.Restrictions
import ru.tesserakt.bot.rainbow.core.commands.Summary
import sx.blah.discord.handle.obj.Permissions

internal class SettingsModule : ModuleBase<CommandContext>() {
    @Command
    @Summary("Устанавливает новый префикс для гильдии")
    @Restrictions(Permissions.ADMINISTRATOR)
    fun prefix(newPrefix : Char) {
        Prefix.register(context.guild, newPrefix)
        context.reply("Префикс для данной гильдии заменен на **$newPrefix**")
    }
}