
import core.RESOURCES
import core.console.EndlessConsoleInput
import core.getParsedObject
import core.handlers.CommandHandler
import core.handlers.ConsoleHandler
import core.handlers.JoinHandler
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