package modules

import command.*
import context.GuildCommandContext
import kotlin.system.exitProcess

class DeveloperModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    @Command
    @Hidden
    @Summary("Вырубает нахрен бота")
    @Aliases("exit", "quit")
    @RequireDeveloper
    fun logout() {
        context.client
            .logout()
            .subscribe()
        exitProcess(0)
    }
}