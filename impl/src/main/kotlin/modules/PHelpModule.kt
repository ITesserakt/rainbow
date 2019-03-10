package modules

import command.Command
import command.ModuleBase
import context.PrivateChannelCommandContext

class PHelpModule : ModuleBase<PrivateChannelCommandContext>() {
    @Command
    fun help() {
        context.reply("Hello!")
    }
}