package modules

import command.*
import context.GuildCommandContext
import kotlinx.coroutines.cancel
import kotlin.system.exitProcess

@RequireDeveloper
class DeveloperModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    @Command
    @Hidden
    @Summary("Вырубает нахрен бота")
    @Aliases("exit", "quit")
    fun logout() {
        context.client
            .logout()
            .subscribe()
        scope.cancel()
        exitProcess(0)
    }
}