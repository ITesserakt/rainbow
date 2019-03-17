package modules

import command.Command
import command.Hidden
import command.ModuleBase
import command.Summary
import context.GuildCommandContext
import util.Database
import util.toSnowflake
import kotlin.system.exitProcess

class DeveloperModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    private val developersIds = arrayOf(316249690092077065.toSnowflake())

    @Command
    @Hidden
    @Summary("Вырубает нахрен бота")
    fun logout() = requireDeveloper {
        context.client
                .logout()
                .subscribe()
        Database.close()
        exitProcess(0)
    }

    private inline fun requireDeveloper(crossinline block: () -> Unit) {
        context.author.map { it.id }
                .subscribe {
                    if (it in developersIds) block()
                    else {
                        context.message.delete().subscribe()
                    }
                }
    }
}