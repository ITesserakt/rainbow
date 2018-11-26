package ru.tesserakt.bot.rainbow.core.console

import ru.tesserakt.bot.rainbow.core.handlers.ConsoleHandler
import sx.blah.discord.api.IDiscordClient

class EndlessConsoleInput (handler : ConsoleHandler, client : IDiscordClient) {
    init {
        while (true) {
            val input = (readLine() ?: continue).split(' ')
            val context = ConsoleCommandContext(client, input)

            handler.OnConsoleCommandReceived(context)
        }
    }
}