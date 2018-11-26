package ru.tesserakt.bot.rainbow.core

import ConfigData
import java.net.URL
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

val VERSION : String = getParsedObject<ConfigData>("config.json").version

object Prefix {
    /**
     * Возвращает стандартный префикс (!)
     */
    fun resolve(): Char = '!'
}

fun getResource(name : String) : URL {
    return Any::class.java.getResource("/$name")
}

fun launch(context : CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
        block.startCoroutine(Continuation(context) { result ->
            result.onFailure { ex ->
                val curThread = Thread.currentThread()
                curThread.uncaughtExceptionHandler.uncaughtException(curThread, ex)
            }
        })