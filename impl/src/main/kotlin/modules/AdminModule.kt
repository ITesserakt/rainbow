package modules

import command.*
import context.GuildCommandContext
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.`object`.util.Snowflake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.find
import kotlinx.coroutines.channels.take
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactive.openSubscription
import util.NoPermissionsException
import util.await
import util.isHigherAsync
import util.toSnowflake
import java.awt.Color
import kotlin.collections.set

@ObsoleteCoroutinesApi
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
    suspend fun ban(member: Member, @Continuous reason: String = "") {
        val botAsMember = context.client.self.flatMap { it.asMember(context.guildId) }.await()
        val guild = context.guild.await()

        if (!botAsMember.isHigherAsync(member) || guild.ownerId == member.id)
            throw NoPermissionsException("Невозможно забанить этого пользователя")

        guild.ban(member.id) { it.reason = reason }.subscribe()

        context.reply(banGives.random())
    }

    @Command
    @Permissions(Permission.KICK_MEMBERS)
    @Aliases("поджопник")
    @Summary("Кикает указанного пользователя")
    suspend fun kick(member: Member, @Continuous reason: String = "") {
        runCatching {
            context.guild.await().kick(member.id, reason).subscribe()
        }.onFailure { context.reply("Невозможно кикнуть этого пользователя") }
    }

    @Command
    @Summary("Разбанивает указанного пользователя")
    @Permissions(Permission.BAN_MEMBERS)
    @Aliases("unban")
    suspend fun pardon(userId: Long, @Continuous reason: String = "") {
        context.guild.await()
            .unban(userId.toSnowflake(), reason)
            .subscribe()
    }

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Command
    @Permissions(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)
    @Summary("Мутит указанного пользователя")
    suspend fun mute(member: Member, `delay in min`: Float) {
        val muteRole = getMuteRoleAsync().await().id
        mutedUsers[context.guildId to member.id] = member.roleIds.toList()

        member.edit { spec ->
            spec.setRoles(setOf(muteRole))
        }.subscribe()

        delay((`delay in min` * 60000).toLong())

        unmute(member)
    }

    @Command
    @Permissions(Permission.BAN_MEMBERS, Permission.KICK_MEMBERS)
    @Summary("Размучивает указанного пользователя")
    fun unmute(@Continuous member: Member) {
        val savedRoles = mutedUsers.remove(context.guildId to member.id)
            ?: throw NoSuchElementException("Пользователь не замучен")

        member.edit {
            it.setRoles(savedRoles.toSet())
        }.subscribe()
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    private fun getMuteRoleAsync() = scope.async {
        val guild = context.guild.await()
        guild.roles.openSubscription().find { it.name == "Muted" }
            ?: guild.createRole { spec ->
                spec.setColor(Color.WHITE)
                    .setPermissions(PermissionSet.none())
                    .setMentionable(false)
                    .setName("Muted")
                    .setHoist(false)
            }.await()
    }

    @ObsoleteCoroutinesApi
    @Command
    @Permissions(Permission.MANAGE_MESSAGES)
    @Summary("Удаляет последние `num` сообщений")
    @Aliases("delete messages")
    suspend fun clear(num: Int): Unit = context.channel.await()
        .getMessagesBefore(context.message.id)
        .openSubscription()
        .take(num)
        .consumeEach {
            it.delete().subscribe()
        }
}