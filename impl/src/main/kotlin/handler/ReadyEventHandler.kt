package handler

import discord4j.core.event.domain.lifecycle.ReadyEvent
import util.Loggers
import util.startedTime
import util.times
import java.time.LocalTime

class ReadyEventHandler : Handler<ReadyEvent>() {
    private val logger = Loggers.getLogger<ReadyEventHandler>()

    override suspend fun handle(event: ReadyEvent) {
        event.client.startedTime = LocalTime.now()

        with(logger) {
            info("=" * 30)
            info("Successfully connected with username ${event.self.username}")
            info("Observing on ${event.guilds.count()} guilds")
            info("=" * 30)
        }
    }
}