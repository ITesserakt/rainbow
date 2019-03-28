package command

import context.ICommandContext
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

    protected suspend fun T.reply(message: String) {
        this.channel.await()
            .createMessage(message)
            .subscribe()
    }

    protected suspend fun T.reply(messageSpec: MessageCreateSpec.() -> Unit) {
        this.channel.await()
            .createMessage(messageSpec)
            .subscribe()
    }
}
