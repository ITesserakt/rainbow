package ru.tesserakt.bot.rainbow.modules

import kotlinx.coroutines.delay
import ru.tesserakt.bot.rainbow.core.ICommandContext
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.launch
import ru.tesserakt.bot.rainbow.core.randomEntry
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import java.awt.Color
import java.net.URL
import java.util.*
import kotlin.NoSuchElementException

internal class AdminsModule : ModuleBase<CommandContext>() {

    private val mutedUsers = arrayListOf<MutedUser>()

    @Command
    @Summary("Банит указанного пользователя")
    @Restrictions(Permissions.BAN)
    fun ban(user: IUser, reason: String = "") {
        val urls = arrayOf(
                "https://i.pinimg.com/originals/5d/77/0c/5d770cbd18e7f9857d1e497a851509b8.gif",
                "https://69.media.tumblr.com/904432c5b044f0588bb31fc49ab8b642/tumblr_ow4mafo4eI1son3fpo2_500.gif",
                "https://thumbs.gfycat.com/UnconsciousDeliciousLcont-max-1mb.gif"
        )

        context.replyFile(URL(urls.randomEntry))
        context.guild.banUser(user, reason)
    }

    @Command
    @Summary("Кикает пользователя")
    @Aliases("поджопник")
    @Restrictions(Permissions.KICK)
    fun kick(user: IUser, @Remainder reason: String = "") {
        context.guild.kickUser(user, reason)
    }

    @Command
    @Summary("Разбанивает указанного пользователя")
    @Restrictions(Permissions.BAN)
    @Aliases("pardon")
    fun unban(userId: Long) {
        context.guild.pardonUser(userId)
    }

    @Command
    @Summary("Мутит указанного пользователя на некоторое время")
    @Restrictions(Permissions.KICK, Permissions.BAN)
    fun mute(user: IUser, `duration in min`: Float) = launch {
        val muteRole = getMuteRole(context)

        mutedUsers.add(MutedUser(user, context.guild))
        context.guild.editUserRoles(user, arrayOf(muteRole))

        delay((`duration in min` * 60000).toLong())

        val mUser = mutedUsers.first { it.user == user && it.guild == context.guild }
        context.guild.editUserRoles(user, mUser.roles)
        mutedUsers.remove(mUser)
    }

    private fun getMuteRole(context: ICommandContext): IRole {
        val guild = context.guild
        return guild.roles.firstOrNull{it.name == "Muted"} ?: guild.createRole().apply {
            changeColor(Color.WHITE)
            changeName("Muted")
            changePermissions(EnumSet.noneOf(Permissions::class.java))
            changeMentionable(false)
        }
    }

    @Command
    @Summary("Размучивает указанного пользователя")
    @Restrictions(Permissions.KICK, Permissions.BAN)
    fun unmute(@Remainder user: IUser) {
        val guild = context.guild
        val mutedUser = mutedUsers.find { it.guild == guild && it.user == user }
                ?: throw NoSuchElementException("Пользователь ${user.name} не замучен!")
        context.guild.editUserRoles(user, mutedUser.roles)
        mutedUsers.remove(mutedUser)
    }

    private class MutedUser(val user: IUser, val guild: IGuild) {
        val roles = guild.getRolesForUser(user).toTypedArray()
    }
}