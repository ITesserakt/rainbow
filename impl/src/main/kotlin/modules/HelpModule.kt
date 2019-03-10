package modules

import command.*
import context.GuildCommandContext
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Permission
import reactor.core.publisher.toMono
import startedTime
import util.RandomColor
import util.toOptional
import java.time.Duration
import java.time.LocalTime

class HelpModule : ModuleBase<GuildCommandContext>() {
    @Command
    @Summary("Выводит список всех команд, если имя команды не передано, иначе - описание команды")
    fun help(`command name`: String = "") {
        `command name`.toMono()
                .filter { it.isBlank() }
                .map { GuildCommandProvider.commands }
                .subscribe {
                    context.reply {
                        setEmbed { spec ->
                            spec.addField("All commands", it.sortedBy { it.name }.joinToString(",\n"), false)
                            spec.setFooter("<...> defines necessary arguments, [...] defines unnecessary", null)
                            spec.setColor(RandomColor)
                        }
                    }
                }

        `command name`.toMono()
                .filter { it.isNotBlank() } //когда просим какую-нибудь команду
                .map { GuildCommandProvider.find(it).toOptional() }
                .subscribe { optCmdInfo ->
                    if (!optCmdInfo.isPresent)
                        context.reply("Данной команды не найдено, используйте `!help` для списка всех команд.")
                    else {
                        val cmdInfo = optCmdInfo.get()
                        context.reply("**$cmdInfo**\n${cmdInfo.description}")
                    }
                }
    }

    @Command
    @Summary("Дополнительная информация о боте")
    fun about() {
        context.reply("""v0.0.7-ALPHA
            |https://github.com/ITesserakt/rainbow
            |Основано на DISCORD4J v3.0.0.M4
        """.trimMargin())
    }

    @Command
    @Summary("Время работы бота")
    fun uptime() {
        context.reply(Duration.between(LocalTime.now(), startedTime).toString())
    }

    @Command("role_info")
    @Summary("Информация об указанной роли")
    @Permissions(Permission.VIEW_AUDIT_LOG)
    fun roleInfo(role: Role) {
        context.reply {
            setEmbed {
                it.setColor(role.color)
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