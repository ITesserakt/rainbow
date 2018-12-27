package ru.tesserakt.bot.rainbow.core

import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.startCoroutine

fun launch(context : CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit) =
        block.startCoroutine(Continuation(context) { result ->
            result.onFailure { ex ->
                val curThread = Thread.currentThread()
                curThread.uncaughtExceptionHandler.uncaughtException(curThread, ex)
            }
        })

val <T> Array<T>.randomEntry : T
    get() {
        val rnd = kotlin.random.Random.nextInt(this.size)
        return this[rnd]
    }

inline fun <reified R> Iterable<R>.dropToArray(n : Int) : Array<R> =
    drop(n).toTypedArray()
