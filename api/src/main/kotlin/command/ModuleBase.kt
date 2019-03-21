package command

import context.ICommandContext
import discord4j.core.spec.MessageCreateSpec
import kotlin.reflect.KClass

abstract class ModuleBase<T : ICommandContext>(val contextType: KClass<T>) {
    protected lateinit var context: T
        private set

    internal fun setContext(context: ICommandContext) {
        @Suppress("UNCHECKED_CAST")
        this.context = context as T
    }

    protected fun T.reply(message: String) {
        this.message.channel.subscribe {
            it.createMessage(message).subscribe()
        }
    }

    protected fun T.reply(messageSpec: MessageCreateSpec.() -> Unit) {
        message.channel.subscribe {
            it.createMessage(messageSpec).subscribe()
        }
    }
}
