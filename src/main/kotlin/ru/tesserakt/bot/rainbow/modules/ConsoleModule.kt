package ru.tesserakt.bot.rainbow.modules

import ru.tesserakt.bot.rainbow.core.Database
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.console.ConsoleCommandContext
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions

class ConsoleModule : ModuleBase<ConsoleCommandContext>() {
    @Command
    @Summary("Выход")
    @Aliases("exit", "logout")
    fun quit() {
        Database.close()
        System.exit(0)
    }

    @Command
    @Summary("Дает доступ к командной строке ")
    fun login(@Remainder guild: IGuild) {
        this.guild = guild
        context.guild = this.guild
    }

    @Command
    @Summary("Дает самую высокую роль, доступную боту")
    @RequireLogin
    @Restrictions(Permissions.MANAGE_ROLES)
    fun shaperize(@Remainder user: IUser) {
        val botRolePos = context.user.getRolesForGuild(context.guild).maxBy { role -> role.position }?.position
        val maxBotRole = context.guild.roles
                .first { role -> botRolePos?.minus(1) == role.position && !role.isEveryoneRole }

        user.addRole(maxBotRole)
    }
}