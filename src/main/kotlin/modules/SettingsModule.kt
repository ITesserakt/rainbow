package modules

import core.ModuleBase
import core.Prefix
import core.commands.CommandService
import core.types.ResolverService

internal class SettingsModule : ModuleBase() {
    init {
        CommandService.addCommand {
            name = "prefix"
            action = {
                val prefix = ResolverService.getForType<Char>().read(it, it.args[0])
                Prefix.register(it.guild, prefix)
                it.reply("Префикс для данной гильдии заменен на **$prefix**")
            }
            summary = "Изменяет префикс для гильдии"
            parameters({ name = "prefix"; build<Char>() })
            build()
        }
    }
}