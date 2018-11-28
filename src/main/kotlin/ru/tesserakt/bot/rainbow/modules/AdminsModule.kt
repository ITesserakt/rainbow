package ru.tesserakt.bot.rainbow.modules

import kotlinx.coroutines.delay
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.launch
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import java.awt.Color
import java.net.URL
import java.util.*

internal class AdminsModule : ModuleBase<CommandContext>() {
    private val mutedUsersDict = HashMap<IUser, Array<IRole>>()

    @Command
    @Summary("Банит указанного пользователя")
    @Restrictions(Permissions.BAN)
    fun ban(user: IUser, reason: String = "") = launch {
        val urls = arrayOf(
                "https://i.pinimg.com/originals/5d/77/0c/5d770cbd18e7f9857d1e497a851509b8.gif",
                "https://69.media.tumblr.com/904432c5b044f0588bb31fc49ab8b642/tumblr_ow4mafo4eI1son3fpo2_500.gif"
        )

        context.replyFile(URL(urls.randomEntry))
        context.guild.banUser(user, reason)
    }

    @Command
    @Summary("Кикает пользователя")
    @Aliases("поджопник")
    @Restrictions(Permissions.KICK)
    fun kick(user: IUser, reason: String = "") {
        context.guild.kickUser(user, reason)
    }

    @Command
    @Summary("Разбанивает указанного пользователя")
    @Restrictions(Permissions.BAN)
    fun unban(userId: Long) {
        context.guild.pardonUser(userId)
    }

    @Command
    @Summary("Мутит указанного пользователя на некоторое время")
    @Restrictions(Permissions.KICK, Permissions.BAN)
    fun mute(user: IUser, `duration in min`: Float) = launch {
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

internal fun IUser.addRoles(roles: Array<out IRole>) {
    for (role in roles)
        this.addRole(role)
}

internal fun IUser.removeRoles(roles: Array<out IRole>) {
    for (role in roles)
        this.removeRole(role)
}

val <T> Array<T>.randomEntry : T
    get() {
        val rnd = kotlin.random.Random.nextInt(this.size)
        return this[rnd]
    }
