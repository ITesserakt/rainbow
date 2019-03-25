package modules

import command.Aliases
import command.Command
import command.ModuleBase
import context.PrivateChannelCommandContext

class PHelpModule : ModuleBase<PrivateChannelCommandContext>(PrivateChannelCommandContext::class) {

    @Command
    @Aliases("test")
    suspend fun hello() {
        context.reply("Hello!")
    }
}