package util

import discord4j.core.event.EventDispatcher
import discord4j.core.event.domain.Event
import org.reflections.Reflections
import reactor.core.publisher.Flux
import reactor.util.Logger
import reactor.util.Loggers
import java.awt.Color
import kotlin.random.Random
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

/**
 * An inline alternative for [EventDispatcher.on]
 */
inline fun <reified T : Event> EventDispatcher.on(): Flux<T> = this.on<T>(T::class.java)

/**
 * Shortcut to quickly determine whether the annotation is present or not
 */
inline fun <reified T : Annotation> KAnnotatedElement.hasAnnotation() = this.findAnnotation<T>() != null

/**
 * An inline alternative for [Loggers.getLogger]
 */
object Loggers {
    /**
     * Get a [Logger], backed by SLF4J if present on the classpath
     */
    inline fun <reified T> getLogger(): Logger = Loggers.getLogger(T::class.java)
}

/**
 * An inline alternative for [Reflections.getSubTypesOf]
 */
inline fun <reified T> Reflections.getSubTypesOf(): Set<Class<out T>> = this.getSubTypesOf(T::class.java)

inline val RandomColor
    inline get() = Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))