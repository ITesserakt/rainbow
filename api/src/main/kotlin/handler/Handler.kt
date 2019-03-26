package handler

import discord4j.core.event.domain.Event
import kotlinx.coroutines.Job

/**
 * Answers on specified event [T] with cancellable [Job]
 */
abstract class Handler <T : Event> {
    abstract fun handle(event: T): Job
}