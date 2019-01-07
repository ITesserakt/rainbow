package ru.tesserakt.bot.rainbow.modules

import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Permission
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.context.GCommandContext
import ru.tesserakt.bot.rainbow.util.version

class HelpModule : ModuleBase<GCommandContext>() {
    @CommandAnn
    @Summary("Выводит список всех команд, если имя команды не передано, иначе - описание команды")
    @Aliases("test")
    fun help(@Remainder cmdName: String = "") {
        if (cmdName.isNotBlank()) {
            val command = GCommandProvider.find(cmdName)
            if (command != null)
                context.reply("**$command**\n*${command.description}*")
            else
                context.reply("Данной команды не существует; введите **!help** для просмотра списка команд")
        } else {
            val cmdStringList = GCommandProvider.commands
                    .map { it.toString() }
                    .joinToString(",\n") { it }

            context.reply("Список всех комманд: \n$cmdStringList")
        }
    }

    @CommandAnn
    @Summary("Дополнительная информация о боте")
    fun about() {
        val version : String = version
        context.reply("$version\nhttps://github.com/ITesserakt/rainbow\nОсновано на DISCORD4J v3-SNAPSHOT")
    }

    @CommandAnn("role_info")
    @Summary("Информация об указанной роли")
    @Permissions(Permission.VIEW_AUDIT_LOG)
    fun roleInfo(@Remainder role: Role) {
        context.reply {
            setContent("")
            setEmbed {
                it.setColor(role.color)
                it.addField("Id", role.id.asString(), true)
                it.addField("Position", role.rawPosition.toString(), false)
            }
        }
    }
}