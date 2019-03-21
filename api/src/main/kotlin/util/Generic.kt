package util

import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.Event
import reactor.core.publisher.Flux
import reactor.util.Logger
import reactor.util.Loggers
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

inline fun <reified T : Event> EventDispatcher.on(): Flux<T> = this.on<T>(T::class.java)


inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation() = this.findAnnotation<T>() != null

object Loggers {
    inline fun <reified T> getLogger(): Logger = Loggers.getLogger(T::class.java)
}