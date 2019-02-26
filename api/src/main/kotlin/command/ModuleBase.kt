package command

import context.ICommandContext
import discord4j.core.spec.MessageCreateSpec

abstract class ModuleBase<T : ICommandContext> {
    protected lateinit var context: T
    internal fun setContext(context: ICommandContext) {
        @Suppress("UNCHECKED_CAST")
        this.context = (context as? T) ?: throw IllegalArgumentException("Неверный тип контекста")
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
