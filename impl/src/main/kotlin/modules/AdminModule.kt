package modules

import command.*
import context.GuildCommandContext
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.`object`.util.Snowflake
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono
import util.NoPermissionsException
import util.and
import util.toSnowflake
import java.awt.Color
import kotlin.collections.set

class AdminModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    private val mutedUsers = hashMapOf<Pair<Snowflake, Snowflake>, List<Snowflake>>()

    private val banGives = arrayOf(
        "https://i.pinimg.com/originals/5d/77/0c/5d770cbd18e7f9857d1e497a851509b8.gif",
        "https://69.media.tumblr.com/904432c5b044f0588bb31fc49ab8b642/tumblr_ow4mafo4eI1son3fpo2_500.gif",
        "https://thumbs.gfycat.com/UnconsciousDeliciousLcont-max-1mb.gif",
        "https://tenor.com/view/banned-thor-gif-6072837"
    )

    @Command
    @Summary("Банит указанного пользователя")
    @Permissions(Permission.BAN_MEMBERS)
    fun ban(member: Member, @Continuous reason: String = ""): Disposable = context.client.self
        .flatMap { it.asMember(context.guildId) }
        .filterWhen { it.isHigher(member) and context.guild.map { guild -> guild.ownerId != member.id } }
        .switchIfEmpty { throw NoPermissionsException("Невозможно забанить этого пользователя") }
        .flatMap { context.guild }
        .flatMap { it.ban(member.id) { spec -> spec.reason = reason } }
        .subscribe {
            context.reply(banGives.random())
        }

    @Command
    @Permissions(Permission.KICK_MEMBERS)
    @Aliases("поджопник")
    @Summary("Кикает указанного пользователя")
    fun kick(member: Member, @Continuous reason: String = ""): Disposable = context.guild.subscribe { guild ->
        guild.kick(member.id, reason)
            .doOnError { context.reply("Невозможно кикнуть этого пользователя") }
            .subscribe()
    }

    @Command
    @Summary("Разбанивает указанного пользователя")
    @Permissions(Permission.BAN_MEMBERS)
    fun pardon(userId: Long, @Continuous reason: String = "") {
        context.guild.subscribe {
            it.unban(userId.toSnowflake(), reason).subscribe()
        }
    }

    @Command
    @Permissions(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)
    @Hidden
    @Summary("Мутит указанного пользователя")
    fun mute(@Continuous member: Member): Disposable = getMuteRole().subscribe {
        mutedUsers[context.guildId to member.id] = member.roleIds.toList()
        member.edit { spec ->
            spec.setRoles(setOf(it))
        }.subscribe()
    }

    @Command
    @Permissions(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)
    @Hidden
    @Summary("Размучивает указанного пользователя")
    fun unmute(@Continuous member: Member) {
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
                        .setHoist(false)
                }
            }
        }.map { it.id }

    @Command
    @Permissions(Permission.MANAGE_MESSAGES)
    @Summary("Удаляет последние `num` сообщений")
    @Aliases("delete messages")
    fun clear(num: Long): Disposable = context.message.channel
        .flatMapMany {
            it.getMessagesBefore(context.message.id)
                .mergeWith(context.message.toMono())
        }.take(num)
        .flatMap { it.delete() }
        .subscribe()
}