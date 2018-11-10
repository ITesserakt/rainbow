package core

import sx.blah.discord.util.EmbedBuilder
import java.io.File

open class ModuleBase {
    /**
     * Отправляет сообщение в канал, из которого оно пришло
     * @param tts прочитать текст вслух?
     */
    fun ICommandContext.reply(message : String, embed : EmbedBuilder? = null, tts : Boolean = false) {
        this.channel.sendMessage(message, embed?.build(), tts)
    }

    /**
     * Отправляет файл в тот же канал с дополнительным сообщением
     */
    fun ICommandContext.replyFile(file : File, message: String = "") {
        this.channel.sendFile(message, file)
    }
}