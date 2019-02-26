package handler

import discord4j.core.event.domain.Event

abstract class Handler <T : Event> {
    abstract fun handle(event : T)
}