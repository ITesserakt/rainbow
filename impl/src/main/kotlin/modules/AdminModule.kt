package modules

import command.Command
import command.ModuleBase
import command.Permissions
import command.Summary
import context.GuildCommandContext
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.`object`.util.Snowflake
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import util.toSnowflake
import java.awt.Color
import kotlin.collections.set
import kotlin.reflect.KClass

class AdminModule : ModuleBase<GuildCommandContext>() {
    override val contextType: KClass<GuildCommandContext> = GuildCommandContext::class
    val mutedUsers = hashMapOf<Pair<Snowflake, Snowflake>, List<Snowflake>>()

    @Command
    @Summary("Банит указанного пользователя")
    @Permissions(Permission.BAN_MEMBERS)
    fun ban(member: Member, reason: String = ""): Disposable = context.guild.subscribe { guild ->
        guild.ban(member.id) { spec -> spec.reason = reason }
                .doOnError { context.reply("Невозможно забанить этого пользователя") }
                .thenReturn(arrayOf(
                        "https://i.pinimg.com/originals/5d/77/0c/5d770cbd18e7f9857d1e497a851509b8.gif",
                        "https://69.media.tumblr.com/904432c5b044f0588bb31fc49ab8b642/tumblr_ow4mafo4eI1son3fpo2_500.gif",
                        "https://thumbs.gfycat.com/UnconsciousDeliciousLcont-max-1mb.gif",
                        "https://tenor.com/view/banned-thor-gif-6072837"
                )).map {
                    context.reply(it.random())
                }
                .subscribe()
    }

    @Command
    @Permissions(Permission.KICK_MEMBERS)
    @Summary("Кикает указанного пользователя")
    fun kick(member: Member, reason: String = ""): Disposable = context.guild.subscribe { guild ->
        guild.kick(member.id, reason)
                .doOnError { context.reply("Невозможно кикнуть этого пользователя") }
                .subscribe()
    }

    @Command
    @Summary("Разбанивает указанного пользователя")
    @Permissions(Permission.BAN_MEMBERS)
    fun pardon(userId: Long, reason: String = "") {
        context.guild.subscribe {
            it.unban(userId.toSnowflake(), reason).subscribe()
        }
    }

    @Command
    @Permissions(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)
    fun mute(member: Member): Disposable = getMuteRole().subscribe {
        mutedUsers[context.guildId to member.id] = member.roleIds.toList()
        member.edit { spec ->
            spec.setRoles(setOf(it))
        }.subscribe()
    }

    @Command
    @Permissions(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)
    fun unmute(member: Member) {
        val savedRoles = mutedUsers.remove(context.guildId to member.id)
                ?: throw NoSuchElementException("Пользователь не замучен")
        member.edit {
            it.setRoles(savedRoles.toSet())
        }.subscribe()
    }

    private fun getMuteRole(): Mono<Snowflake> = context.guild
            .flatMapMany { it.roles }
            .filter { it.name == "Muted" }.next()
            .switchIfEmpty {
                context.guild.flatMap {
                    it.createRole { spec ->
                        spec.setColor(Color.WHITE)
                                .setPermissions(PermissionSet.none())
                                .setMentionable(false)
                                .setName("Muted")
                    }
                }
            }.map { it.id }
}

internal class MutedUser internal constructor(val guildId : String, val userId : String, val roles : Array<String>)