package ru.tesserakt.bot.rainbow.modules

import ru.tesserakt.bot.rainbow.ConfigData
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import sx.blah.discord.Discord4J
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.Permissions
import sx.blah.discord.util.EmbedBuilder

class HelpModule : ModuleBase<CommandContext>() {

    @Command
    @Summary("Выводит список всех команд, если имя команды не передано, иначе - описание команды")
    @Aliases("test")
    fun help(cmdName: String = "") {
        if (cmdName.isNotBlank()) {
            val command = CommandService.getCommandByName(cmdName)
                    ?: CommandService.getCommandByAlias(cmdName)
            if (command != null)
                context.reply("**$command**\n*${command.summary}*")
            else
                context.reply("Данной команды не существует; введите **!help** для просмотра списка команд")
        } else {
            val cmdStringList = CommandService.commandsList
                    .map { it.toString() }
                    .joinToString(",\n") { it }

            context.reply("Список всех комманд: \n$cmdStringList")
        }
    }

    @Command
    @Summary("Допонительная информация о боте")
    fun about() {
        val version : String = ConfigData.version
        context.reply("$version\nhttps://github.com/ITesserakt/rainbow\nОсновано на DISCORD4Jv${Discord4J.VERSION}")
    }

    @Command
    @Summary("Информация об указанной роли")
    @Restrictions(Permissions.VIEW_AUDIT_LOG)
    fun role_info(@Remainder role: IRole) {
        context.reply("", EmbedBuilder()
                .withColor(role.color)
                .appendField("Id", role.stringID, true)
                .appendField("Position", role.position.toString(), true)
                .appendField("Created", role.creationDate.toString(), false))
    }
}