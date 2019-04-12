package handler

import discord4j.core.event.domain.Event

/**
 * Answers on specified event [T]
 */
abstract class Handler<T : Event> {
    abstract suspend fun handle(event: T)
}