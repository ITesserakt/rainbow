package modules

import core.ModuleBase
import core.Prefix
import core.VERSION
import core.commands.CommandService
import core.types.ResolverService
import sx.blah.discord.Discord4J
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.util.EmbedBuilder

class HelpModule : ModuleBase() {
    init {
        CommandService.addCommand{
            name = "help"
            action = {context ->
                if (!context.args.isEmpty()) {
                    val command = CommandService.getCommandByName(context.args[0])
                            ?: CommandService.getCommandByAlias(context.args[0])
                    if (command != null)
                        context.reply("**$command**\n*${command.summary}*")
                    else
                        context.reply("Данной команды не существует; введите **${Prefix.resolve(context.guild)}help** для просмотра списка команд")
                } else {
                    val cmdStringList = CommandService.commandsList
                            .map { it.toString() }
                            .joinToString(",\n") { it }

                    context.reply("Список всех комманд: \n$cmdStringList")
                }
            }
            summary = "Выводит список всех команд, если имя команды не передано, иначе - описание команды"
            aliases = arrayOf("test")
            parameters({name = "command name"; isOptional = true; build<String>()})
            build()
        }

        CommandService.addCommand{
            name = "about"
            action = {
                it.reply("$VERSION\nОсновано на DISCORD4Jv${Discord4J.VERSION}")
            }
            summary = "Информация о боте"
            build()
        }

        CommandService.addCommand {
            name = "role_info"
            action = {
                val role = ResolverService.getForType<IRole>().readToEnd(it, it.args)
                it.reply("", EmbedBuilder()
                        .withColor(role.color)
                        .appendField("Id", role.stringID, true)
                        .appendField("Position", role.position.toString(), false)
                        .appendField("Created", role.creationDate.toString(), false))
            }
            summary = "Информация об указанной роли"
            parameters({name = "role"; build<IRole>()})
            build()
        }
    }
}