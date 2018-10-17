package core

import core.commands.ICommandContext

open class ModuleBase<T : ICommandContext> {
    fun T.reply(message : String, tts : Boolean = false /*TODO добавить embed*/) {
        this.channel.sendMessage(message, tts)
    }
}