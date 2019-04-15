package util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import reactor.core.publisher.*

@ExperimentalCoroutinesApi
internal class CoroutineKtTest : Assertions() {

    @Test
    fun `await on non-empty mono and expect value`() = runBlocking {
        val mono = Mono.just(1)
        assertEquals(mono.awaitSingle(), 1)
    }

    @Test
    fun `await on empty mono and expect error`() {
        val mono = Mono.empty<Int>()
        assertThrows(NoSuchElementException::class.java) {
            runBlocking {
                mono.awaitSingle()
            }
        }
    }

    @Test
    fun `await on mono with data and expect value`() = runBlocking {
        val mono = Mono.just(1)
        assertEquals(mono.awaitFirstOrNull(), 1)
    }

    @Test
    fun `await on empty mono and expect null`() = runBlocking {
        val mono = Mono.empty<Int>()
        assertEquals(mono.awaitFirstOrNull(), null)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `await all values from flux`() = runBlocking {
        val flux = Flux.just(1, 2, 3, 4, 5)
        assertEquals(flux.openSubscription().toList(), listOf(1, 2, 3, 4, 5))
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `await empty flux, expect null`() = runBlocking {
        val flux = Flux.empty<Int>()
        assertEquals(flux.openSubscription().receiveOrNull(), null)
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `await 10 elements from infinity flux`() = runBlocking {
        var i = 0
        val channel = Flux.generate<Int> { it.next(++i) }.openSubscription()
        val list = List(10) {
            channel.receive()
        }

        assertEquals(list, listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
    }

    @Test
    fun `error in mono, expect same error in result`() {
        val mono = 1.toMono()
            .filter { it == 2 }
            .switchIfEmpty { throw NullPointerException() }

        assertThrows(NullPointerException::class.java) {
            runBlocking {
                mono.awaitFirstOrNull()
            }
        }

        assertThrows(NullPointerException::class.java) {
            runBlocking {
                mono.awaitSingle()
            }
        }
    }

    @ObsoleteCoroutinesApi
    @Test
    fun `error in flux, expect same error in channel`() {
        val flux = arrayOf(1, 2, 3, 4).toFlux()
            .filter { it == 5 }
            .switchIfEmpty { throw NullPointerException() }

        assertThrows(NullPointerException::class.java) {
            runBlocking {
                flux.openSubscription().receive()
            }
        }
    }
}