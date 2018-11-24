
import ru.tesserakt.bot.rainbow.core.RESOURCES
import ru.tesserakt.bot.rainbow.core.console.EndlessConsoleInput
import ru.tesserakt.bot.rainbow.core.getParsedObject
import ru.tesserakt.bot.rainbow.core.handlers.CommandHandler
import ru.tesserakt.bot.rainbow.core.handlers.ConsoleHandler
import ru.tesserakt.bot.rainbow.core.handlers.JoinHandler
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.handle.obj.ActivityType
import sx.blah.discord.handle.obj.StatusType

class RegisterBot {
    init {
        val config = getParsedObject<ConfigData>("$RESOURCES/config.json")
        val builder = ClientBuilder().withToken(config.token)
        val client = builder.build()

        client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, "Say /help")
        client.dispatcher.registerListener(CommandHandler())
        client.dispatcher.registerListener(JoinHandler())
        client.login()

        val handler = ConsoleHandler()
        EndlessConsoleInput(handler, client)
    }
}