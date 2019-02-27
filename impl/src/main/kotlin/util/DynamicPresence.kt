package util

import discord4j.core.DiscordClient
import discord4j.core.`object`.presence.Activity
import discord4j.core.`object`.presence.Presence
import reactor.core.publisher.Flux
import reactor.core.publisher.toMono
import reactor.core.scheduler.Schedulers
import reactor.util.function.Tuple2
import reactor.util.function.component1
import reactor.util.function.component2
import java.lang.management.ManagementFactory
import java.time.Duration

class DynamicPresence (private val client : DiscordClient, delay : Duration) {
    private val presenceFlux: Flux<Tuple2<Long, Any>> = Flux.merge(
            client.users.count(),
            client.guilds.count(),
            client.regions.count(),
            client.guilds.flatMap { it.channels }.count(),
            ManagementFactory.getThreadMXBean().threadCount.toMono()
    ).index().delayElements(delay)

    private val words = arrayOf("users", "guilds", "regions", "channels", "threads")

    fun start(): Flux<String> = presenceFlux.map { (index, value) ->
        "$value ${words[index.toInt()]}"
    }.doOnNext {
        client.updatePresence(Presence.online(Activity.watching(it))).subscribe()
    }.repeat().subscribeOn(Schedulers.elastic())
}