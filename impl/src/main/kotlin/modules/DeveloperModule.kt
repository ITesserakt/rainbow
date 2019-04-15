package modules

import command.*
import context.GuildCommandContext
import kotlinx.coroutines.cancel
import logoutAsync
import kotlin.system.exitProcess

@RequireDeveloper
class DeveloperModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    @Command
    @Hidden
    @Summary("Вырубает нахрен бота")
    @Aliases("exit", "quit")
    suspend fun logout() {
        context.client.logoutAsync()
        scope.cancel()
        exitProcess(0)
    }
}