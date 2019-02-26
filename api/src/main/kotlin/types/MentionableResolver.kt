package types

import context.ICommandContext
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty

abstract class MentionableResolver<T> : ITypeResolver<T> {
    abstract fun mentionMatch(context: ICommandContext, input: String): Mono<T>
    abstract fun idMatch(context: ICommandContext, input: String): Mono<T>
    abstract fun elseMatch(context: ICommandContext, input: String): Mono<T>
    abstract val exceptionMessage : String

    override fun read(context: ICommandContext, input: String): Mono<T> {
        return when {
            Regex("""^<.\d{18}>$""").matches(input) -> mentionMatch(context, input)
            Regex("""^\d{18}$""").matches(input) -> idMatch(context, input)
            else -> elseMatch(context, input)
        }.switchIfEmpty { throw NoSuchElementException(exceptionMessage) }
    }
}