package types

import context.ICommandContext
import kotlinx.coroutines.Deferred

/**
 * A parser based on mention and it components
 */
abstract class MentionableResolver<T : Any> : ITypeResolver<T> {
    abstract fun mentionMatchAsync(context: ICommandContext, input: String): Deferred<T?>
    abstract fun idMatchAsync(context: ICommandContext, input: String): Deferred<T?>
    abstract fun elseMatchAsync(context: ICommandContext, input: String): Deferred<T?>
    abstract val exceptionMessage: String

    override suspend fun read(context: ICommandContext, input: String): T {
        return when {
            Regex("""^<.\d{18}>$""").matches(input) -> mentionMatchAsync(context, input)
            Regex("""^\d{18}$""").matches(input) -> idMatchAsync(context, input)
            else -> elseMatchAsync(context, input)
        }.await() ?: throw NoSuchElementException(exceptionMessage)
    }
}