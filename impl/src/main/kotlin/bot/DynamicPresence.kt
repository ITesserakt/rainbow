package bot

import discord4j.core.DiscordClient
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import kotlinx.coroutines.*
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import util.await
import java.time.Duration

internal class DynamicPresence(private val client: DiscordClient, private val delay: Duration) {
    private val presenceArray: Array<Mono<*>> = arrayOf(
        client.users.count(),
        client.guilds.count(),
        client.regions.count(),
        client.guilds.flatMap { it.channels }.count(),
        Thread.activeCount().toMono()
    )
    private val words = arrayOf("users", "guilds", "regions", "channels", "threads")

    fun start() = GlobalScope.launch(Dispatchers.IO) {
        var index = 0
        while (isActive) {
            val activity = "${presenceArray[index].await()} ${words[index]} | say !help"
            client.updatePresence(Presence.online(Activity.watching(activity))).subscribe()

            delay(delay.toMillis())
            index = (index + 1) % 5
        }
    }
}