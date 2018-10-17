
import core.commands.CommandHandler
import core.getParsedObject
import sx.blah.discord.api.ClientBuilder

class RegisterBot {
    init {
        val config = getParsedObject<ConfigData>("src/main/resources/config.json")
        val builder = ClientBuilder().withToken(config.token)

        val client = builder.build()
        client.dispatcher.registerListener(CommandHandler())
        client.login()
    }
}