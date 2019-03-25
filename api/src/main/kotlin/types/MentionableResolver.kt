package types

import context.ICommandContext
import kotlinx.coroutines.Deferred
import util.toOptional

abstract class MentionableResolver<T> : ITypeResolver<T> {
    abstract fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<T?>
    abstract fun idMatchAsync(context: ICommandContext, input: String): Deferred<T?>
    abstract fun elseMatchAsync(context: ICommandContext, input: String): Deferred<T?>
    abstract val exceptionMessage : String

    override suspend fun read(context: ICommandContext, input: String): T {
        return when {
            Regex("""^<.\d{18}>$""").matches(input) -> mentionMatchAsync(context, input)
            Regex("""^\d{18}$""").matches(input) -> idMatchAsync(context, input)
            else -> elseMatchAsync(context, input)
        }.await()
            .toOptional()
            .orElseThrow { NoSuchElementException(exceptionMessage) }
    }
}