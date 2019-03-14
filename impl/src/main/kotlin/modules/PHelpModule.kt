package modules

import command.Command
import command.ModuleBase
import context.PrivateChannelCommandContext
import kotlin.reflect.KClass

class PHelpModule : ModuleBase<PrivateChannelCommandContext>() {
    override val contextType: KClass<PrivateChannelCommandContext> = PrivateChannelCommandContext::class

    @Command
    fun help() {
        context.reply("Hello!")
    }
}