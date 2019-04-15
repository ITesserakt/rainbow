@file:Suppress("unused")

import discord4j.common.json.GuildEmojiResponse
import discord4j.common.json.GuildMemberResponse
import discord4j.common.json.MessageResponse
import discord4j.common.json.UserResponse
import discord4j.core.DiscordClient
import discord4j.core.`object`.Ban
import discord4j.core.`object`.Invite
import discord4j.core.`object`.entity.*
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.`object`.util.Snowflake
import discord4j.rest.json.response.*
import discord4j.rest.service.*
import kotlinx.coroutines.reactive.awaitFirstOrNull

/**
 * @see [DiscordClient.getMemberById]
 */
suspend inline fun DiscordClient.getMemberByIdAsync(arg0: Snowflake, arg1: Snowflake): Member? =
    this.getMemberById(arg0, arg1).awaitFirstOrNull()

/**
 * @see [DiscordClient.getInvite]
 */
suspend inline fun DiscordClient.getInviteAsync(arg0: String): Invite? =
    this.getInvite(arg0).awaitFirstOrNull()

/**
 * @see [DiscordClient.getGuildEmojiById]
 */
suspend inline fun DiscordClient.getGuildEmojiByIdAsync(arg0: Snowflake, arg1: Snowflake):
        GuildEmoji? = this.getGuildEmojiById(arg0, arg1).awaitFirstOrNull()

/**
 * @see [DiscordClient.getChannelById]
 */
suspend inline fun DiscordClient.getChannelByIdAsync(arg0: Snowflake): Channel? =
    this.getChannelById(arg0).awaitFirstOrNull()

/**
 * @see [DiscordClient.getMessageById]
 */
suspend inline fun DiscordClient.getMessageByIdAsync(arg0: Snowflake, arg1: Snowflake): Message? =
    this.getMessageById(arg0, arg1).awaitFirstOrNull()

/**
 * @see [DiscordClient.getGuildById]
 */
suspend inline fun DiscordClient.getGuildByIdAsync(arg0: Snowflake): Guild? =
    this.getGuildById(arg0).awaitFirstOrNull()

/**
 * @see [DiscordClient.getUserById]
 */
suspend inline fun DiscordClient.getUserByIdAsync(arg0: Snowflake): User? =
    this.getUserById(arg0).awaitFirstOrNull()

/**
 * @see [DiscordClient.getRoleById]
 */
suspend inline fun DiscordClient.getRoleByIdAsync(arg0: Snowflake, arg1: Snowflake): Role? =
    this.getRoleById(arg0, arg1).awaitFirstOrNull()

/**
 * @see [DiscordClient.getWebhookById]
 */
suspend inline fun DiscordClient.getWebhookByIdAsync(arg0: Snowflake): Webhook? =
    this.getWebhookById(arg0).awaitFirstOrNull()

/**
 * @see [Guild.getMemberById]
 */
suspend inline fun Guild.getMemberByIdAsync(arg0: Snowflake): Member? =
    this.getMemberById(arg0).awaitFirstOrNull()

/**
 * @see [Guild.getGuildEmojiById]
 */
suspend inline fun Guild.getGuildEmojiByIdAsync(arg0: Snowflake): GuildEmoji? =
    this.getGuildEmojiById(arg0).awaitFirstOrNull()

/**
 * @see [Guild.getChannelById]
 */
suspend inline fun Guild.getChannelByIdAsync(arg0: Snowflake): GuildChannel? =
    this.getChannelById(arg0).awaitFirstOrNull()

/**
 * @see [Guild.getBan]
 */
suspend inline fun Guild.getBanAsync(arg0: Snowflake): Ban? = this.getBan(arg0).awaitFirstOrNull()

/**
 * @see [Guild.getRoleById]
 */
suspend inline fun Guild.getRoleByIdAsync(arg0: Snowflake): Role? =
    this.getRoleById(arg0).awaitFirstOrNull()

/**
 * @see [Guild.getPruneCount]
 */
suspend inline fun Guild.getPruneCountAsync(arg0: Int): Int? =
    this.getPruneCount(arg0).awaitFirstOrNull()

/**
 * @see [GuildChannel.getEffectivePermissions]
 */
suspend inline fun GuildChannel.getEffectivePermissionsAsync(arg0: Snowflake): PermissionSet? =
    this.getEffectivePermissions(arg0).awaitFirstOrNull()

/**
 * @see [MessageChannel.getMessageById]
 */
suspend inline fun MessageChannel.getMessageByIdAsync(arg0: Snowflake): Message? =
    this.getMessageById(arg0).awaitFirstOrNull()

/**
 * @see [TextChannel.getMessageById]
 */
suspend inline fun TextChannel.getMessageByIdAsync(arg0: Snowflake): Message? =
    this.getMessageById(arg0).awaitFirstOrNull()

/**
 * @see [TextChannel.getEffectivePermissions]
 */
suspend inline fun TextChannel.getEffectivePermissionsAsync(arg0: Snowflake): PermissionSet? =
    this.getEffectivePermissions(arg0).awaitFirstOrNull()

/**
 * @see [AuditLogService.getAuditLog]
 */
suspend inline fun AuditLogService.getAuditLogAsync(arg0: Long, arg1: Map<String, Any?>): AuditLogResponse? =
    this.getAuditLog(arg0, arg1).awaitFirstOrNull()

/**
 * @see [ChannelService.getMessage]
 */
suspend inline fun ChannelService.getMessageAsync(arg0: Long, arg1: Long): MessageResponse? =
    this.getMessage(arg0, arg1).awaitFirstOrNull()

/**
 * @see [ChannelService.getChannel]
 */
suspend inline fun ChannelService.getChannelAsync(arg0: Long): ChannelResponse? =
    this.getChannel(arg0).awaitFirstOrNull()

/**
 * @see [EmojiService.getGuildEmoji]
 */
suspend inline fun EmojiService.getGuildEmojiAsync(arg0: Long, arg1: Long): GuildEmojiResponse? =
    this.getGuildEmoji(arg0, arg1).awaitFirstOrNull()

/**
 * @see [GuildService.getGuildMember]
 */
suspend inline fun GuildService.getGuildMemberAsync(arg0: Long, arg1: Long): GuildMemberResponse? =
    this.getGuildMember(arg0, arg1).awaitFirstOrNull()

/**
 * @see [GuildService.getGuildBan]
 */
suspend inline fun GuildService.getGuildBanAsync(arg0: Long, arg1: Long): BanResponse? =
    this.getGuildBan(arg0, arg1).awaitFirstOrNull()

/**
 * @see [GuildService.getGuild]
 */
suspend inline fun GuildService.getGuildAsync(arg0: Long): GuildResponse? =
    this.getGuild(arg0).awaitFirstOrNull()

/**
 * @see [GuildService.getGuildEmbed]
 */
suspend inline fun GuildService.getGuildEmbedAsync(arg0: Long): GuildEmbedResponse? =
    this.getGuildEmbed(arg0).awaitFirstOrNull()

/**
 * @see [GuildService.getGuildPruneCount]
 */
suspend inline fun GuildService.getGuildPruneCountAsync(arg0: Long, arg1: Map<String, Any?>): PruneResponse? =
    this.getGuildPruneCount(arg0, arg1).awaitFirstOrNull()

/**
 * @see [InviteService.getInvite]
 */
suspend inline fun InviteService.getInviteAsync(arg0: String): InviteResponse? =
    this.getInvite(arg0).awaitFirstOrNull()

/**
 * @see [UserService.getUser]
 */
suspend inline fun UserService.getUserAsync(arg0: Long): UserResponse? =
    this.getUser(arg0).awaitFirstOrNull()

/**
 * @see [WebhookService.getWebhook]
 */
suspend inline fun WebhookService.getWebhookAsync(arg0: Long): WebhookResponse? =
    this.getWebhook(arg0).awaitFirstOrNull()
