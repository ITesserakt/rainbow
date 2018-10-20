package core

import core.commands.CommandContext
import java.io.File

open class ModuleBase<T : CommandContext> {
    fun T.reply(message : String, tts : Boolean = false /*TODO добавить embed*/) {
        this.channel.sendMessage(message, tts)
    }

    fun T.replyFile(file : File, message: String = "") {
        this.channel.sendFile(message, file)
    }
}