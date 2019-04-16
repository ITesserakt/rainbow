package command

import context.ICommandContext
import createEmbedAsync
import createMessageAsync
import discord4j.core.spec.EmbedCreateSpec
import discord4j.core.spec.MessageCreateSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.reflect.KClass

abstract class ModuleBase<T : ICommandContext>(val contextType: KClass<T>) {
    protected lateinit var context: T
        private set

    protected val scope = CoroutineScope(Dispatchers.Unconfined)

    internal fun setContext(context: ICommandContext) {
        @Suppress("UNCHECKED_CAST")
        this.context = context as T
    }

    protected suspend fun T.reply(message: String) =
        this.channel.await().createMessageAsync(message)

    protected suspend fun T.reply(messageSpec: MessageCreateSpec.() -> Unit) =
        this.channel.await().createMessageAsync(messageSpec)

    protected suspend fun T.replyEmbed(embedSpec: EmbedCreateSpec.() -> Unit) =
        channel.await().createEmbedAsync(embedSpec)
}
