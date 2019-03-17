package modules

import command.Command
import command.ModuleBase
import context.PrivateChannelCommandContext

class PHelpModule : ModuleBase<PrivateChannelCommandContext>(PrivateChannelCommandContext::class) {

    @Command
    fun help() {
        context.reply("Hello!")
    }
}