package core.console

import core.handlers.ConsoleHandler
import org.slf4j.LoggerFactory
import sx.blah.discord.api.IDiscordClient

class EndlessConsoleInput (handler : ConsoleHandler, client : IDiscordClient) {
    init {
        var logged = false
        val logger = LoggerFactory.getLogger(EndlessConsoleInput::class.java)

        while (true) {
            val input = (readLine() ?: continue).split(' ')
            if (input[0] != "login" && !logged) {
                logger.error("'Login' first!")
                continue
            } else if (input[0] == "login") logged = true

            val context = ConsoleCommandContext(client, input)

            handler.OnConsoleCommandReceived(context)
        }
    }
}