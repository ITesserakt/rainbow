package modules

import core.ModuleBase
import core.Prefix
import core.commands.CommandService
import core.types.ResolverService
import sx.blah.discord.handle.obj.Permissions

internal class SettingsModule : ModuleBase() {
    init {
        CommandService.addCommand {
            name = "prefix"
            action = {
                val prefix = ResolverService.parse<Char>(it, 0)
                Prefix.register(it.guild, prefix)
                it.reply("Префикс для данной гильдии заменен на **$prefix**")
            }
            restrictions(Permissions.ADMINISTRATOR)
            summary = "Изменяет префикс для гильдии"
            parameters({ name = "prefix"; build<Char>() })
            build()
        }
    }
}