package modules

import command.*
import context.PrivateChannelCommandContext
import kotlinx.coroutines.cancel
import logoutAsync
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
    suspend fun logout() {
        context.client.logoutAsync()
        scope.cancel()
        exitProcess(0)
    }
}