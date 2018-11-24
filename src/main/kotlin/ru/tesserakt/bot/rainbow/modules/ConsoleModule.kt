package ru.tesserakt.bot.rainbow.modules

import ru.tesserakt.bot.rainbow.core.ICommandContext
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.Aliases
import ru.tesserakt.bot.rainbow.core.commands.Command
import ru.tesserakt.bot.rainbow.core.commands.Summary
import ru.tesserakt.bot.rainbow.core.console.ConsoleCommandContext
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

class ConsoleModule : ModuleBase<ConsoleCommandContext>() {
    private lateinit var guild: IGuild

    @Command
    @Summary("Выход")
    @Aliases("exit")
    fun quit() {
        System.exit(0)
    }

    @Command
    @Summary("Дает доступ к командной строке ")
    fun login(guild: IGuild) {
        this.guild = guild
        context.guild = this.guild
    }

    @Command
    @Summary("Дает самую высокую роль, доступную боту")
    fun shaperize(user: IUser) {
        updateLateInitProps(context)

        val botRolePos = context.user.getRolesForGuild(context.guild).maxBy { role -> role.position }?.position
        val maxBotRole = context.guild.roles
                .first { role -> botRolePos?.minus(1) == role.position && !role.isEveryoneRole }

        user.addRole(maxBotRole)
    }

    private fun updateLateInitProps(context: ICommandContext) {
        if (::guild.isInitialized) {
            context.guild = guild
        } else throw IllegalArgumentException("'Login' first!")
    }
}