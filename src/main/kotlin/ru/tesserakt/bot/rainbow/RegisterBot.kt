package ru.tesserakt.bot.rainbow

import ru.tesserakt.bot.rainbow.core.console.EndlessConsoleInput
import ru.tesserakt.bot.rainbow.core.handlers.CommandHandler
import ru.tesserakt.bot.rainbow.core.handlers.ConsoleHandler
import ru.tesserakt.bot.rainbow.core.handlers.JoinHandler
import sx.blah.discord.api.ClientBuilder

class RegisterBot {
    init {
        val config = ConfigData.token
        val client = ClientBuilder()
                .withToken(config)
                .registerListener(CommandHandler())
                .registerListener(JoinHandler())
                .build()

        client.login()

        val handler = ConsoleHandler()
        EndlessConsoleInput(handler, client)
    }
}