
import ru.tesserakt.bot.rainbow.core.RESOURCES
import ru.tesserakt.bot.rainbow.core.console.EndlessConsoleInput
import ru.tesserakt.bot.rainbow.core.getParsedObject
import ru.tesserakt.bot.rainbow.core.handlers.CommandHandler
import ru.tesserakt.bot.rainbow.core.handlers.ConsoleHandler
import ru.tesserakt.bot.rainbow.core.handlers.JoinHandler
import sx.blah.discord.api.ClientBuilder

class RegisterBot {
    init {
        val config = getParsedObject<ConfigData>("${RESOURCES}config.json")
        val client = ClientBuilder()
                .withToken(config.token)
                .registerListener(CommandHandler())
                .registerListener(JoinHandler())
                .build()

        client.login()

        val handler = ConsoleHandler()
        EndlessConsoleInput(handler, client)
    }
}