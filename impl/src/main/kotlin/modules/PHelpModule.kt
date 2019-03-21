package modules

import command.Aliases
import command.Command
import command.Hidden
import command.ModuleBase
import context.PrivateChannelCommandContext

class PHelpModule : ModuleBase<PrivateChannelCommandContext>(PrivateChannelCommandContext::class) {

    @Command
    @Aliases("test")
    @Hidden
    @Deprecated("Does not work", ReplaceWith("HelpModule.help()"), DeprecationLevel.ERROR)
    fun hello() {
        context.reply("Hello!")
    }
}