package modules

import core.ModuleBase
import core.commands.CommandContext
import core.commands.CommandService
import core.registerPrefix
import core.types.ResolverService

class SettingsModule : ModuleBase<CommandContext>() {
    init {
        CommandService.addCommand {
            name = "prefix"
            action = {
                val prefix = ResolverService.getForType<Char>().read(it, it.args[0])
                registerPrefix(it.guild, prefix)
                it.reply("Префикс для данной гильдии заменен на **$prefix**")
            }
            summary = "Изменяет префикс для гильдии"
            parameters({ name = "prefix"; build<Char>() })
            build()
        }
    }
}