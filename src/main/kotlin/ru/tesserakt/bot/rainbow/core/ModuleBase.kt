package ru.tesserakt.bot.rainbow.core

import sx.blah.discord.util.EmbedBuilder
import java.io.File

@Suppress("UNCHECKED_CAST")
abstract class ModuleBase<T : ICommandContext> {
    /**
     * Отправляет сообщение в канал, из которого оно пришло
     * @param tts прочитать текст вслух?
     */
    fun T.reply(message: String, embed: EmbedBuilder? = null, tts: Boolean = false) {
        this.channel.sendMessage(message, embed?.build(), tts)
    }

    /**
     * Отправляет файл в тот же канал с дополнительным сообщением
     */
    fun T.replyFile(file: File, message: String = "") {
        this.channel.sendFile(message, file)
    }

    lateinit var context : T
        private set

    internal fun setContextInternal(value : ICommandContext){
        val newContext = value as? T
        context = newContext ?: throw IllegalArgumentException("Неверный тип контекста")
    }
}