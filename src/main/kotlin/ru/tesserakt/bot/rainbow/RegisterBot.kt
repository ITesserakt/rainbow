package ru.tesserakt.bot.rainbow

import ru.tesserakt.bot.rainbow.core.console.EndlessConsoleInput
import ru.tesserakt.bot.rainbow.core.handlers.CommandHandler
import ru.tesserakt.bot.rainbow.core.handlers.ConsoleHandler
import ru.tesserakt.bot.rainbow.core.handlers.JoinHandler
import sx.blah.discord.api.ClientBuilder
import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.ActivityType
import sx.blah.discord.handle.obj.StatusType
import kotlin.concurrent.timer

class RegisterBot {
    init {
        val config = ConfigData.token
        val client = ClientBuilder()
                .withToken(config)
                .registerListener(CommandHandler())
                .registerListener(JoinHandler())
                .build()

        client.login()

        setTimedPresence(client)

        val handler = ConsoleHandler()
        EndlessConsoleInput(handler, client)
    }

    private fun setTimedPresence(client: IDiscordClient) {
        var index = 0

        timer("Presence", true, 0L, 5000) {
            val state = when (index) {
                0 -> "${client.guilds.size} guilds"
                1 -> "${client.users.size} users"
                2 -> "${client.regions.size} regions"
                3 -> "${client.channels.size} channels"
                4 -> "${client.categories.size} categories"
                else -> ""
            }
            client.changePresence(StatusType.ONLINE, ActivityType.LISTENING, "$state | say !help")

            index++
            if (index >= 5) index = 0
        }
    }
}