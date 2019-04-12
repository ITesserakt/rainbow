package util

import discord4j.core.event.domain.lifecycle.ReadyEvent
import handler.Handler
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import java.time.Duration

class EventKtTest {
    private var eventFlux = Flux.interval(
        Duration.ofSeconds(1)
    ).map {
        mockk<ReadyEvent>()
    }

    private val handler = object : Handler<ReadyEvent>() {
        override suspend fun handle(event: ReadyEvent) {
            println(event.hashCode())
        }
    }

    @Test
    fun `subscribe to handler and then dispose`() = runBlocking {
        eventFlux += handler
        delay(5000)
        eventFlux -= handler
    }

    @Test
    fun `subscribe to lambda and then dispose`() = runBlocking {
        eventFlux += {
            println(it.hashCode())
        }
    }
}