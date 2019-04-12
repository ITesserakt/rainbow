package util

import discord4j.core.event.domain.Event
import handler.Handler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import reactor.core.Disposable
import reactor.core.publisher.Flux

private val scope = CoroutineScope(Dispatchers.IO)
private val eventTable = hashMapOf<Handler<out Event>, Disposable>()

/**
 * Adds new handler to [Event].
 *
 * Uses for C# event style
 */
operator fun <T : Event> Flux<T>.plusAssign(handler: Handler<T>) {
    eventTable[handler] = subscribe {
        scope.launch { handler.handle(it) }
    }
}

@Deprecated("Because of a lot of bugs")
operator fun <T : Event> Flux<T>.plusAssign(callback: suspend (event: T) -> Unit) {
    val handler = object : Handler<T>() {
        override suspend fun handle(event: T) =
            callback(event)
    }
    this += handler
}

operator fun <T : Event> Flux<T>.minusAssign(handler: Handler<T>) {
    eventTable.getOrElse(handler) {
        throw NoSuchElementException("Событие не подписано")
    }.dispose()
}