package ru.tesserakt.bot.rainbow.modules

import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.CommandAnn
import ru.tesserakt.bot.rainbow.core.commands.Summary
import ru.tesserakt.bot.rainbow.core.context.GCommandContext
import ru.tesserakt.bot.rainbow.util.Database

class DeveloperModule : ModuleBase<GCommandContext>() {
    @CommandAnn
    @Summary("Выключает бота")
    fun logout() = requireOwner {
        context.client.logout()
        Database.close()
        System.exit(0)
    }

    private fun requireOwner(block: () -> Unit) {
        context.message.authorAsMember
                .map { it.id.asLong() == 316249690092077065L }
                .subscribe {
                    if (it)
                        block()
                    else
                        context.reply("Только владелец бота может сделать это!")
                }
    }
}