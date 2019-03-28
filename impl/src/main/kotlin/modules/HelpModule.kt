package modules

import command.*
import context.GuildCommandContext
import discord4j.common.GitProperties.APPLICATION_VERSION
import discord4j.common.GitProperties.getProperties
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Permission
import startedTime
import util.RandomColor
import java.time.Duration
import java.time.LocalTime

class HelpModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    @Command
    @Aliases("test")
    @Summary("Выводит список всех команд, если имя команды не передано, иначе - описание команды")
    suspend fun help(@Continuous `command name`: String = "") {
        val cmdName = `command name`.replace(' ', '_')
        if (cmdName.isBlank()) {
            val commands = GuildCommandProvider.commands
                .sortedBy { it.name }
                .joinToString(", \n")

            context.reply {
                setEmbed { spec ->
                    spec.addField("All commands", commands, false)
                    spec.setFooter("<...> defines necessary arguments, [...] defines unnecessary", null)
                    spec.setColor(RandomColor)
                }
            }
        } else {
            val command = GuildCommandProvider.find(`command name`)
            if (command == null)
                context.reply("Данной команды не найдено, используйте `!help` для списка всех команд.")
            else
                context.reply("**$command**\n${command.description}")
        }
    }

    @Command
    @Summary("Дополнительная информация о боте")
    suspend fun about() {
        context.reply(
            """v0.0.8-ALPHA
            |https://github.com/ITesserakt/rainbow
            |Основано на DISCORD4J ${getProperties()[APPLICATION_VERSION]}
        """.trimMargin())
    }

    @Command
    @Summary("Время работы бота")
    suspend fun uptime() {
        context.reply(
            "Бот работает уже ${Duration.between(LocalTime.now(), startedTime)}"
        )
    }

    @Command("role_info")
    @Summary("Информация об указанной роли")
    @Permissions(Permission.VIEW_AUDIT_LOG)
    suspend fun roleInfo(@Continuous role: Role) {
        context.reply {
            setEmbed {
                it.setColor(role.color)
                it.setTitle(role.name)
                it.addField("Id", role.id.asString(), true)
                it.addField("Position", role.rawPosition.toString(), true)
                it.addField("Mention", "`${role.mention}`", true)
                it.addField("Permissions",
                        role.permissions
                                .joinToString { perm -> perm.name.toLowerCase().replace('_', ' ') }
                                .ifEmpty { "No permissions (╯°□°）╯︵ ┻━┻" }, true)
            }
        }
    }
}