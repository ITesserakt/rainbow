package ru.tesserakt.bot.rainbow.modules

import kotlinx.coroutines.delay
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.RESOURCES
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.launch
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import java.awt.Color
import java.io.File
import java.util.*

internal class AdminsModule : ModuleBase<CommandContext>() {
    private val mutedUsersDict = HashMap<IUser, Array<IRole>>()

    @Command
    @Summary("Банит указанного пользователя")
    @Restrictions(Permissions.BAN)
    fun ban(user: IUser, reason: String = "") {
        context.guild.banUser(user, reason)
        context.replyFile(File("$RESOURCES/omae_wa_mou.gif"), "")
    }

    @Command
    @Summary("Кикает пользователя")
    @Aliases("Поджопник")
    @Restrictions(Permissions.KICK)
    fun kick(user : IUser, reason : String = "") {
        context.guild.kickUser(user, reason)
    }

    @Command
    @Summary("Разбанивает указанного пользователя")
    @Restrictions(Permissions.BAN)
    fun unban(userId : Long) {
        context.guild.pardonUser(userId)
    }

    @Command
    @Summary("Мутит указанного пользователя на некоторое время")
    @Restrictions(Permissions.KICK, Permissions.BAN)
    fun mute(user : IUser, `duration in min` : Float) = launch {
        val muteRole = getMuteRole(context)
        val savedRoles = user.getRolesForGuild(context.guild).toTypedArray()

        mutedUsersDict[user] = savedRoles
        user.removeRoles(savedRoles)
        user.addRole(muteRole)

        delay((`duration in min` * 60000).toLong())

        user.removeRole(muteRole)
        user.addRoles(savedRoles)
        mutedUsersDict.remove(user)
    }

    @Command
    @Summary("Размучивает указанного пользователя")
    @Restrictions(Permissions.KICK, Permissions.BAN)
    fun unmute(user: IUser) {
        val muteRole = getMuteRole(context)
        val savedRoles = mutedUsersDict.getValue(user)

        user.removeRole(muteRole)
        user.addRoles(savedRoles)
    }

    private fun getMuteRole(it: CommandContext): IRole {
        val muteRole = it.guild.getRolesByName("Muted").first() ?: it.guild.createRole()

        with(muteRole) {
            changeColor(Color.WHITE)
            changeName("Muted")
            changeMentionable(false)
            changePermissions(EnumSet.noneOf(Permissions::class.java))
        }
        return muteRole
    }
}
    private fun IUser.addRoles(roles: Array<out IRole>) {
        for (role in roles)
            this.addRole(role)
    }

    private fun IUser.removeRoles(roles: Array<out IRole>) {
        for (role in roles)
            this.removeRole(role)
    }