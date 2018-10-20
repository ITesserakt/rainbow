
import core.getParsedObject
import core.handlers.CommandHandler
import core.handlers.JoinHandler
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.handle.obj.ActivityType
import sx.blah.discord.handle.obj.StatusType

class RegisterBot {
    init {
        val config = getParsedObject<ConfigData>("src/main/resources/config.json")
        val builder = ClientBuilder().withToken(config.token)
        val client = builder.build()

        client.changePresence(StatusType.ONLINE, ActivityType.WATCHING, "Say /help")
        client.dispatcher.registerListener(CommandHandler())
        client.dispatcher.registerListener(JoinHandler())
        client.login()
    }
}