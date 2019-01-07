package ru.tesserakt.bot.rainbow.core

import discord4j.core.spec.MessageCreateSpec
import ru.tesserakt.bot.rainbow.core.context.ICommandContext
import java.net.URL

abstract class ModuleBase<T : ICommandContext> {
    protected lateinit var context : T

    internal fun setContextInternal(value : ICommandContext) {
        @Suppress("UNCHECKED_CAST")
        val newContext = value as? T
        context = newContext ?: throw IllegalArgumentException("Неверный тип контекста")
    }

    protected fun T.reply(message : String) {
        this.message.channel
                .subscribe { it.createMessage(message).subscribe() }
    }

    protected fun T.reply(file : URL, message: String = "") {
        this.message.channel
                .subscribe { it.createMessage { spec ->
                    spec.setFile(file.file, file.openStream())
                    spec.setContent(message)
                }.subscribe() }
    }

    protected fun T.reply(spec : MessageCreateSpec.() -> Unit) {
        this.message.channel
                .subscribe { it.createMessage(spec).subscribe() }
    }
}