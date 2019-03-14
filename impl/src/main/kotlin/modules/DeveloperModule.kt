package modules

import command.Command
import command.ModuleBase
import command.Summary
import context.GuildCommandContext
import discord4j.core.`object`.util.Snowflake
import util.Database
import kotlin.reflect.KClass

class DeveloperModule : ModuleBase<GuildCommandContext>() {
    override val contextType: KClass<GuildCommandContext> = GuildCommandContext::class
    private val developersIds = arrayOf<Snowflake>(Snowflake.of(316249690092077065))

    @Command
    @Summary("Вырубает нахрен бота")
    fun logout() = requireDeveloper {
        context.client
                .logout()
                .subscribe()
        Database.close()
        System.exit(0)
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