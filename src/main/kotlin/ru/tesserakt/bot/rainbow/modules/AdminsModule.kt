package ru.tesserakt.bot.rainbow.modules

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import kotlinx.coroutines.delay
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toFlux
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.context.GCommandContext
import ru.tesserakt.bot.rainbow.util.*
import java.awt.Color

class AdminsModule : ModuleBase<GCommandContext>() {

    private val mutedUsers = arrayListOf<MutedUser>()

    @CommandAnn
    @Summary("Банит указанного пользователя")
    @Permissions(Permission.BAN_MEMBERS)
    fun ban(user: User, @Remainder reason: String = "") {
        val urls = arrayOf(
                "https://i.pinimg.com/originals/5d/77/0c/5d770cbd18e7f9857d1e497a851509b8.gif",
                "https://69.media.tumblr.com/904432c5b044f0588bb31fc49ab8b642/tumblr_ow4mafo4eI1son3fpo2_500.gif",
                "https://thumbs.gfycat.com/UnconsciousDeliciousLcont-max-1mb.gif",
                "https://tenor.com/view/banned-thor-gif-6072837"
        )

        context.reply(urls.random())
        context.guild.map { guild ->
            guild.ban(user.id) {
                it.reason = reason
            }.subscribe()
        }.subscribe()
    }

    @CommandAnn
    @Permissions(Permission.BAN_MEMBERS)
    @Summary("Разбанивает указанного пользователя")
    fun unban(user: User) {
        context.guild.map {
            it.unban(user.id).subscribe()
        }.subscribe()
    }

    @CommandAnn
    @Permissions(Permission.KICK_MEMBERS)
    @Summary("Кикает пользователя")
    @Aliases("поджопник")
    fun kick(user: User, @Remainder reason: String = "") {
        context.guild.map {
            it.kick(user.id, reason).subscribe()
        }.subscribe()
    }

    //FIXME
    @Deprecated("Недоработано", level = DeprecationLevel.ERROR)
    @CommandAnn
    @Summary("Мутит указанного пользователя на некоторое время")
    @Permissions(Permission.KICK_MEMBERS, Permission.BAN_MEMBERS)
    fun mute(user: User, `duration in min`: Float) = launch {
        val muteRole = getMuteRole(context)

        mutedUsers.add(MutedUser(user, context.guild))
        context.guild
                .flatMap { it.getMemberById(user.id) }
                .zipWith(muteRole)
                .map { (member, role) ->
                    member.edit {
                        it.setRoles(setOf(role.id))
                    }.subscribe()
                }.subscribe()

        val mUser = mutedUsers.first { it.user == user && it.guild == context.guild }

        delay((60000L * `duration in min`).toLong())

        context.guild
                .flatMap { it.getMemberById(user.id) }
                .zipWith(mUser.roles.collectList())
                .map { (member, roles) ->
                    member.edit {
                        it.setRoles(roles.map { role -> role.id }.toSet())
                    }.subscribe()
                }.subscribe()

        mutedUsers.remove(mUser)
    }

    private fun getMuteRole(context: GCommandContext): Mono<Role> {
        val guild = context.guild
        return guild.flatMap { it.getRolesByName("Muted").next().subscribeWithoutDisposable() }
                .switchIfEmpty {
                    guild.flatMap {
                        it.createRole { createSpec ->
                            createSpec.setColor(Color.WHITE)
                            createSpec.setName("Muted")
                            createSpec.setPermissions(PermissionSet.none())
                            createSpec.setMentionable(false)
                        }
                    }
                }.subscribeWithoutDisposable()
    }

    //FIXME
    @CommandAnn
    @Summary("Размучивает указанного пользователя")
    @Permissions(Permission.KICK_MEMBERS, Permission.BAN_MEMBERS)
    fun unmute(@Remainder user: User) {
        val guild = context.guild
        val mutedUser = mutedUsers.toFlux()
                .filter { it.user == user && it.guild == guild }.next()
                .switchIfEmpty {
                    throw NoSuchElementException("Не найдено ни одной подходящей роли!")
                }

        guild.flatMap { it.getMemberById(user.id) }
                .zipWith(mutedUser.flatMap { it.roles.collectList() })
                .map { (member, roles) ->
                    member.edit {
                        it.setRoles(roles.map { role -> role.id }.toSet())
                    }.subscribe()
                }
                .flatMap { mutedUser }
                .subscribe { mutedUsers.remove(it) }
    }

    private class MutedUser(val user: User, val guild: Mono<Guild>) {
        val roles: Flux<Role> = guild
                .flatMap {
                    it.getMemberById(user.id)
                }
                .toFlux()
                .flatMap(Member::getRoles)
                .subscribeWithoutDisposable()
    }
}