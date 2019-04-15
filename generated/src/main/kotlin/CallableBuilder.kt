import com.squareup.kotlinpoet.FileSpec
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import kotlin.reflect.KCallable

typealias CallablePub = KCallable<Publisher<*>>
typealias CallableMono = KCallable<Mono<*>>
typealias CallableFlux = KCallable<Flux<*>>

interface CallableBuilder<T : CallablePub> {
    val fileSpec: FileSpec.Builder
    fun build(list: List<T>): FileSpec.Builder
}
