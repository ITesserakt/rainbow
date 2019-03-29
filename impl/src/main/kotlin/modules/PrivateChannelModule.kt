package modules

import command.*
import context.PrivateChannelCommandContext
import kotlinx.coroutines.cancel
import kotlin.system.exitProcess

class PrivateChannelModule : ModuleBase<PrivateChannelCommandContext>(PrivateChannelCommandContext::class) {
    @Command
    @Aliases("test")
    suspend fun hello() {
        context.reply("Hello!")
    }

    @Command
    @RequireDeveloper
    @Summary("Выключает бота")
    @Aliases("exit", "quit")
    fun logout() {
        context.client
            .logout()
            .subscribe()
        scope.cancel()
        exitProcess(0)
    }
}