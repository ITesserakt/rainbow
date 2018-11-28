package ru.tesserakt.bot.rainbow.core

import org.slf4j.LoggerFactory
import ru.tesserakt.bot.rainbow.modules.ConsoleModule
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.util.EmbedBuilder
import java.net.URL

@Suppress("UNCHECKED_CAST")
abstract class ModuleBase<T : ICommandContext>{
    protected lateinit var guild : IGuild

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
    fun T.replyFile(url: URL, message: String = "") {
        this.channel.sendFile(message, url.openStream(), url.file)
    }

    internal fun updateLateInitProps() : Boolean{
        val logger = LoggerFactory.getLogger(ConsoleModule::class.java)

        if (::guild.isInitialized) {
            this.context.guild = guild
            return true
        }
        logger.error("Войдите в консоль сначала!")
        return false
    }

    lateinit var context : T
        private set

    internal fun setContextInternal(value : ICommandContext){
        val newContext = value as? T
        context = newContext ?: throw IllegalArgumentException("Неверный тип контекста")
    }
}