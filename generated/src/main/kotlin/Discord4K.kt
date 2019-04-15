@file:Suppress("unused")

import discord4j.common.json.*
import discord4j.core.DiscordClient
import discord4j.core.`object`.*
import discord4j.core.`object`.entity.*
import discord4j.core.`object`.presence.Presence
import discord4j.core.`object`.reaction.ReactionEmoji
import discord4j.core.`object`.trait.Categorizable
import discord4j.core.`object`.trait.Invitable
import discord4j.core.`object`.util.PermissionSet
import discord4j.core.`object`.util.Snowflake
import discord4j.core.event.dispatch.DispatchContext
import discord4j.core.event.dispatch.DispatchHandler
import discord4j.core.event.domain.Event
import discord4j.core.event.domain.PresenceUpdateEvent
import discord4j.core.event.domain.VoiceServerUpdateEvent
import discord4j.core.event.domain.WebhooksUpdateEvent
import discord4j.core.event.domain.channel.PinsUpdateEvent
import discord4j.core.event.domain.channel.TypingStartEvent
import discord4j.core.event.domain.guild.*
import discord4j.core.event.domain.message.*
import discord4j.core.event.domain.role.RoleCreateEvent
import discord4j.core.event.domain.role.RoleDeleteEvent
import discord4j.core.shard.ShardAwareStore
import discord4j.core.spec.*
import discord4j.gateway.DefaultGatewayClient
import discord4j.gateway.DiscordWebSocketHandler
import discord4j.gateway.GatewayClient
import discord4j.gateway.GatewayObserver
import discord4j.gateway.json.GatewayPayload
import discord4j.gateway.json.dispatch.Dispatch
import discord4j.gateway.payload.JacksonPayloadReader
import discord4j.gateway.payload.JacksonPayloadWriter
import discord4j.rest.http.*
import discord4j.rest.http.client.ClientRequest
import discord4j.rest.http.client.DiscordWebClient
import discord4j.rest.json.request.*
import discord4j.rest.json.response.*
import discord4j.rest.request.DefaultRouter
import discord4j.rest.request.DiscordRequest
import discord4j.rest.request.Router
import discord4j.rest.service.*
import discord4j.rest.util.MultipartRequest
import discord4j.store.api.Store
import discord4j.store.api.noop.NoOpStore
import discord4j.store.api.noop.primitive.NoOpLongObjStore
import discord4j.store.api.primitive.ForwardingStore
import discord4j.store.api.primitive.LongObjStore
import discord4j.store.api.readonly.ReadOnlyStore
import discord4j.store.api.readonly.primitive.ReadOnlyLongObjStore
import discord4j.store.api.util.LongObjTuple2
import discord4j.store.jdk.JdkStore
import discord4j.voice.VoiceConnection
import io.netty.buffer.ByteBuf
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.reactive.awaitSingle
import org.reactivestreams.Publisher
import reactor.netty.ByteBufMono
import reactor.netty.http.client.HttpClient
import reactor.netty.http.client.HttpClientResponse
import reactor.netty.http.websocket.WebsocketInbound
import reactor.netty.http.websocket.WebsocketOutbound
import reactor.util.function.Tuple2
import java.awt.Color
import java.io.Serializable
import java.util.function.Consumer

/**
 * @see [DiscordClient.edit]
 */
suspend inline fun DiscordClient.editAsync(noinline arg0: UserEditSpec.() -> Unit): User = this.edit(arg0).awaitSingle()

/**
 * @see [DiscordClient.createGuild]
 */
suspend inline fun DiscordClient.createGuildAsync(noinline arg0: GuildCreateSpec.() -> Unit): Guild =
    this.createGuild(arg0).awaitSingle()

/**
 * @see [DiscordClient.updatePresence]
 */
suspend inline fun DiscordClient.updatePresenceAsync(arg0: Presence): Void =
    this.updatePresence(arg0).awaitSingle()

/**
 * @see [DispatchHandler.handle]
 */
suspend inline fun <D : Dispatch, E : Event> DispatchHandler<D, E>.handleAsync(arg0: DispatchContext<D>): E =
    this.handle(arg0).awaitSingle()

/**
 * @see [Category.edit]
 */
suspend inline fun Category.editAsync(noinline arg0: CategoryEditSpec.() -> Unit): Category =
    this.edit(arg0).awaitSingle()

/**
 * @see [Channel.delete]
 */
suspend inline fun Channel.deleteAsync(arg0: String) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [Guild.edit]
 */
suspend inline fun Guild.editAsync(noinline arg0: GuildEditSpec.() -> Unit): Guild = this.edit(arg0).awaitSingle()

/**
 * @see [Guild.unban]
 */
suspend inline fun Guild.unbanAsync(arg0: Snowflake) {
    this.unban(arg0).awaitSingle()
}

/**
 * @see [Guild.unban]
 */
suspend inline fun Guild.unbanAsync(arg0: Snowflake, arg1: String): Void = this.unban(
    arg0,
    arg1
).awaitSingle()

/**
 * @see [Guild.prune]
 */
suspend inline fun Guild.pruneAsync(arg0: Int): Int = this.prune(arg0).awaitSingle()

/**
 * @see [Guild.prune]
 */
suspend inline fun Guild.pruneAsync(arg0: Int, arg1: String): Int = this.prune(
    arg0,
    arg1
).awaitSingle()

/**
 * @see [Guild.createVoiceChannel]
 */
suspend inline fun Guild.createVoiceChannelAsync(noinline arg0: VoiceChannelCreateSpec.() -> Unit): VoiceChannel =
    this.createVoiceChannel(arg0).awaitSingle()

/**
 * @see [Guild.createCategory]
 */
suspend inline fun Guild.createCategoryAsync(noinline arg0: CategoryCreateSpec.() -> Unit): Category =
    this.createCategory(arg0).awaitSingle()

/**
 * @see [Guild.changeSelfNickname]
 */
suspend inline fun Guild.changeSelfNicknameAsync(arg0: String): String =
    this.changeSelfNickname(arg0).awaitSingle()

/**
 * @see [Guild.createEmoji]
 */
suspend inline fun Guild.createEmojiAsync(noinline arg0: GuildEmojiCreateSpec.() -> Unit): GuildEmoji =
    this.createEmoji(arg0).awaitSingle()

/**
 * @see [Guild.createTextChannel]
 */
suspend inline fun Guild.createTextChannelAsync(noinline arg0: TextChannelCreateSpec.() -> Unit): TextChannel =
    this.createTextChannel(arg0).awaitSingle()

/**
 * @see [Guild.kick]
 */
suspend inline fun Guild.kickAsync(arg0: Snowflake) {
    this.kick(arg0).awaitSingle()
}

/**
 * @see [Guild.kick]
 */
suspend inline fun Guild.kickAsync(arg0: Snowflake, arg1: String): Void = this.kick(
    arg0,
    arg1
).awaitSingle()

/**
 * @see [Guild.createRole]
 */
suspend inline fun Guild.createRoleAsync(noinline arg0: RoleCreateSpec.() -> Unit): Role =
    this.createRole(arg0).awaitSingle()

/**
 * @see [Guild.ban]
 */
suspend inline fun Guild.banAsync(arg0: Snowflake, noinline arg1: BanQuerySpec.() -> Unit): Void = this.ban(
    arg0,
    arg1
).awaitSingle()

/**
 * @see [GuildChannel.addRoleOverwrite]
 */
suspend inline fun GuildChannel.addRoleOverwriteAsync(arg0: Snowflake, arg1: PermissionOverwrite) {
    this.addRoleOverwrite(arg0, arg1).awaitSingle()
}

/**
 * @see [GuildChannel.addRoleOverwrite]
 */
suspend inline fun GuildChannel.addRoleOverwriteAsync(
    arg0: Snowflake,
    arg1: PermissionOverwrite,
    arg2: String
) {
    this.addRoleOverwrite(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [GuildChannel.addMemberOverwrite]
 */
suspend inline fun GuildChannel.addMemberOverwriteAsync(
    arg0: Snowflake,
    arg1: PermissionOverwrite,
    arg2: String
) {
    this.addMemberOverwrite(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [GuildChannel.addMemberOverwrite]
 */
suspend inline fun GuildChannel.addMemberOverwriteAsync(arg0: Snowflake, arg1: PermissionOverwrite) {

    this.addMemberOverwrite(arg0, arg1).awaitSingle()
}

/**
 * @see [GuildEmoji.edit]
 */
suspend inline fun GuildEmoji.editAsync(noinline arg0: GuildEmojiEditSpec.() -> Unit): GuildEmoji =
    this.edit(arg0).awaitSingle()

/**
 * @see [GuildEmoji.delete]
 */
suspend inline fun GuildEmoji.deleteAsync(arg0: String) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [Member.isHigher]
 */
suspend inline fun Member.isHigherAsync(arg0: Member): Boolean = this.isHigher(arg0).awaitSingle()

/**
 * @see [Member.isHigher]
 */
suspend inline fun Member.isHigherAsync(arg0: Snowflake): Boolean =
    this.isHigher(arg0).awaitSingle()

/**
 * @see [Member.asMember]
 */
suspend inline fun Member.asMemberAsync(arg0: Snowflake): Member = this.asMember(arg0).awaitSingle()

/**
 * @see [Member.edit]
 */
suspend inline fun Member.editAsync(arg0: Consumer<GuildMemberEditSpec>) {
    this.edit(arg0).awaitSingle()
}

/**
 * @see [Member.unban]
 */
suspend inline fun Member.unbanAsync(arg0: String) {
    this.unban(arg0).awaitSingle()
}

/**
 * @see [Member.removeRole]
 */
suspend inline fun Member.removeRoleAsync(arg0: Snowflake): Void =
    this.removeRole(arg0).awaitSingle()

/**
 * @see [Member.removeRole]
 */
suspend inline fun Member.removeRoleAsync(arg0: Snowflake, arg1: String): Void =
    this.removeRole(arg0, arg1).awaitSingle()

/**
 * @see [Member.kick]
 */
suspend inline fun Member.kickAsync(arg0: String) {
    this.kick(arg0).awaitSingle()
}

/**
 * @see [Member.hasHigherRoles]
 */
suspend inline fun Member.hasHigherRolesAsync(arg0: Iterable<Role>): Boolean =
    this.hasHigherRoles(arg0).awaitSingle()

/**
 * @see [Member.addRole]
 */
suspend inline fun Member.addRoleAsync(arg0: Snowflake, arg1: String): Void = this.addRole(
    arg0,
    arg1
).awaitSingle()

/**
 * @see [Member.addRole]
 */
suspend inline fun Member.addRoleAsync(arg0: Snowflake) {
    this.addRole(arg0).awaitSingle()
}

/**
 * @see [Member.ban]
 */
suspend inline fun Member.banAsync(noinline arg0: BanQuerySpec.() -> Unit) {
    this.ban(arg0).awaitSingle()
}

/**
 * @see [Message.edit]
 */
suspend inline fun Message.editAsync(noinline arg0: MessageEditSpec.() -> Unit): Message = this.edit(arg0).awaitSingle()

/**
 * @see [Message.addReaction]
 */
suspend inline fun Message.addReactionAsync(arg0: ReactionEmoji): Void =
    this.addReaction(arg0).awaitSingle()

/**
 * @see [Message.removeSelfReaction]
 */
suspend inline fun Message.removeSelfReactionAsync(arg0: ReactionEmoji): Void =
    this.removeSelfReaction(arg0).awaitSingle()

/**
 * @see [Message.delete]
 */
suspend inline fun Message.deleteAsync(arg0: String) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [Message.removeReaction]
 */
suspend inline fun Message.removeReactionAsync(arg0: ReactionEmoji, arg1: Snowflake): Void =
    this.removeReaction(arg0, arg1).awaitSingle()

/**
 * @see [MessageChannel.createMessage]
 */
suspend inline fun MessageChannel.createMessageAsync(arg0: String): Message =
    this.createMessage(arg0).awaitSingle()

/**
 * @see [MessageChannel.createMessage]
 */
suspend inline fun MessageChannel.createMessageAsync(noinline arg0: MessageCreateSpec.() -> Unit): Message =
    this.createMessage(arg0).awaitSingle()

/**
 * @see [MessageChannel.createEmbed]
 */
suspend inline fun MessageChannel.createEmbedAsync(noinline arg0: EmbedCreateSpec.() -> Unit): Message =
    this.createEmbed(arg0).awaitSingle()

/**
 * @see [Role.edit]
 */
suspend inline fun Role.editAsync(noinline arg0: RoleEditSpec.() -> Unit): Role = this.edit(arg0).awaitSingle()

/**
 * @see [Role.delete]
 */
suspend inline fun Role.deleteAsync(arg0: String) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [TextChannel.createInvite]
 */
suspend inline fun TextChannel.createInviteAsync(noinline arg0: InviteCreateSpec.() -> Unit): ExtendedInvite =
    this.createInvite(arg0).awaitSingle()

/**
 * @see [TextChannel.addRoleOverwrite]
 */
suspend inline fun TextChannel.addRoleOverwriteAsync(
    arg0: Snowflake,
    arg1: PermissionOverwrite,
    arg2: String
) {
    this.addRoleOverwrite(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [TextChannel.createMessage]
 */
suspend inline fun TextChannel.createMessageAsync(noinline arg0: MessageCreateSpec.() -> Unit): Message =
    this.createMessage(arg0).awaitSingle()

/**
 * @see [TextChannel.edit]
 */
suspend inline fun TextChannel.editAsync(noinline arg0: TextChannelEditSpec.() -> Unit): TextChannel =
    this.edit(arg0).awaitSingle()

/**
 * @see [TextChannel.createWebhook]
 */
suspend inline fun TextChannel.createWebhookAsync(noinline arg0: WebhookCreateSpec.() -> Unit): Webhook =
    this.createWebhook(arg0).awaitSingle()

/**
 * @see [TextChannel.addMemberOverwrite]
 */
suspend inline fun TextChannel.addMemberOverwriteAsync(
    arg0: Snowflake,
    arg1: PermissionOverwrite,
    arg2: String
) {
    this.addMemberOverwrite(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [User.asMember]
 */
suspend inline fun User.asMemberAsync(arg0: Snowflake): Member = this.asMember(arg0).awaitSingle()

/**
 * @see [VoiceChannel.createInvite]
 */
suspend inline fun VoiceChannel.createInviteAsync(noinline arg0: InviteCreateSpec.() -> Unit): ExtendedInvite =
    this.createInvite(arg0).awaitSingle()

/**
 * @see [VoiceChannel.edit]
 */
suspend inline fun VoiceChannel.editAsync(noinline arg0: VoiceChannelEditSpec.() -> Unit): VoiceChannel =
    this.edit(arg0).awaitSingle()

/**
 * @see [VoiceChannel.join]
 */
suspend inline fun VoiceChannel.joinAsync(noinline arg0: VoiceChannelJoinSpec.() -> Unit): VoiceConnection =
    this.join(arg0).awaitSingle()

/**
 * @see [Webhook.edit]
 */
suspend inline fun Webhook.editAsync(noinline arg0: WebhookEditSpec.() -> Unit): Webhook = this.edit(arg0).awaitSingle()

/**
 * @see [Webhook.delete]
 */
suspend inline fun Webhook.deleteAsync(arg0: String) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [ExtendedPermissionOverwrite.delete]
 */
suspend inline fun ExtendedPermissionOverwrite.deleteAsync(arg0: String): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [Invite.delete]
 */
suspend inline fun Invite.deleteAsync(arg0: String) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [Invitable.createInvite]
 */
suspend inline fun Invitable.createInviteAsync(noinline arg0: InviteCreateSpec.() -> Unit): ExtendedInvite =
    this.createInvite(arg0).awaitSingle()

/**
 * @see [ShardAwareStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ShardAwareStore<K, V>.saveAsync(arg0: Publisher<Tuple2<K, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [ShardAwareStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ShardAwareStore<K, V>.saveAsync(arg0: K, arg1: V): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [ShardAwareStore.find]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ShardAwareStore<K, V>.findAsync(arg0: K): V =
    this.find(arg0).awaitSingle()

/**
 * @see [ShardAwareStore.deleteInRange]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ShardAwareStore<K, V>.deleteInRangeAsync(arg0: K, arg1: K) {
    this.deleteInRange(arg0, arg1).awaitSingle()
}

/**
 * @see [ShardAwareStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ShardAwareStore<K, V>.deleteAsync(arg0: Publisher<K>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [DiscordWebClient.exchange]
 */
suspend inline fun <T, R> DiscordWebClient.exchangeAsync(
    arg0: ClientRequest,
    arg1: Any,
    arg2: Class<T>,
    arg3: Consumer<HttpClientResponse>
): T = this.exchange(arg0, arg1, arg2, arg3).awaitSingle()

/**
 * @see [EmptyReaderStrategy.read]
 */
suspend inline fun EmptyReaderStrategy.readAsync(arg0: ByteBufMono, arg1: Class<Void>): Void =
    this.read(arg0, arg1).awaitSingle()

/**
 * @see [EmptyWriterStrategy.write]
 */
suspend inline fun EmptyWriterStrategy.writeAsync(arg0: HttpClient.RequestSender, arg1: Void):
        HttpClient.ResponseReceiver<*> = this.write(arg0, arg1).awaitSingle()

/**
 * @see [FallbackReaderStrategy.read]
 */
suspend inline fun FallbackReaderStrategy.readAsync(arg0: ByteBufMono, arg1: Class<String>): String =
    this.read(arg0, arg1).awaitSingle()

/**
 * @see [JacksonReaderStrategy.read]
 */
suspend inline fun <Res> JacksonReaderStrategy<Res>.readAsync(arg0: ByteBufMono, arg1: Class<Res>): Res =
    this.read(arg0, arg1).awaitSingle()

/**
 * @see [JacksonWriterStrategy.write]
 */
suspend inline fun JacksonWriterStrategy.writeAsync(arg0: HttpClient.RequestSender, arg1: Any):
        HttpClient.ResponseReceiver<*> = this.write(arg0, arg1).awaitSingle()

/**
 * @see [MultipartWriterStrategy.write]
 */
suspend inline fun MultipartWriterStrategy.writeAsync(
    arg0: HttpClient.RequestSender,
    arg1: MultipartRequest
): HttpClient.ResponseReceiver<*> = this.write(arg0, arg1).awaitSingle()

/**
 * @see [ReaderStrategy.read]
 */
suspend inline fun <Res> ReaderStrategy<Res>.readAsync(arg0: ByteBufMono, arg1: Class<Res>): Res =
    this.read(arg0, arg1).awaitSingle()

/**
 * @see [WriterStrategy.write]
 */
suspend inline fun <Req> WriterStrategy<Req>.writeAsync(arg0: HttpClient.RequestSender, arg1: Req):
        HttpClient.ResponseReceiver<*> = this.write(arg0, arg1).awaitSingle()

/**
 * @see [DefaultRouter.exchange]
 */
suspend inline fun <T> DefaultRouter.exchangeAsync(arg0: DiscordRequest<T>): T =
    this.exchange(arg0).awaitSingle()

/**
 * @see [DiscordRequest.exchange]
 */
suspend inline fun <T> DiscordRequest<T>.exchangeAsync(arg0: Router): T =
    this.exchange(arg0).awaitSingle()

/**
 * @see [Router.exchange]
 */
suspend inline fun <T> Router.exchangeAsync(arg0: DiscordRequest<T>): T =
    this.exchange(arg0).awaitSingle()

/**
 * @see [ChannelService.deleteGroupDMRecipient]
 */
suspend inline fun ChannelService.deleteGroupDMRecipientAsync(arg0: Long, arg1: Long): Void =
    this.deleteGroupDMRecipient(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.createMessage]
 */
suspend inline fun ChannelService.createMessageAsync(arg0: Long, arg1: MultipartRequest):
        MessageResponse = this.createMessage(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.deletePinnedMessage]
 */
suspend inline fun ChannelService.deletePinnedMessageAsync(arg0: Long, arg1: Long): Void =
    this.deletePinnedMessage(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.triggerTypingIndicator]
 */
suspend inline fun ChannelService.triggerTypingIndicatorAsync(arg0: Long): Void =
    this.triggerTypingIndicator(arg0).awaitSingle()

/**
 * @see [ChannelService.createChannelInvite]
 */
suspend inline fun ChannelService.createChannelInviteAsync(
    arg0: Long,
    arg1: InviteCreateRequest,
    arg2: String
): InviteResponse = this.createChannelInvite(arg0, arg1, arg2).awaitSingle()

/**
 * @see [ChannelService.deleteAllReactions]
 */
suspend inline fun ChannelService.deleteAllReactionsAsync(arg0: Long, arg1: Long): Void =
    this.deleteAllReactions(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.addGroupDMRecipient]
 */
suspend inline fun ChannelService.addGroupDMRecipientAsync(
    arg0: Long,
    arg1: Long,
    arg2: GroupAddRecipientRequest
) {
    this.addGroupDMRecipient(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [ChannelService.editChannelPermissions]
 */
suspend inline fun ChannelService.editChannelPermissionsAsync(
    arg0: Long,
    arg1: Long,
    arg2: PermissionsEditRequest,
    arg3: String
) {
    this.editChannelPermissions(arg0, arg1, arg2, arg3).awaitSingle()
}

/**
 * @see [ChannelService.editMessage]
 */
suspend inline fun ChannelService.editMessageAsync(
    arg0: Long,
    arg1: Long,
    arg2: MessageEditRequest
): MessageResponse = this.editMessage(arg0, arg1, arg2).awaitSingle()

/**
 * @see [ChannelService.deleteOwnReaction]
 */
suspend inline fun ChannelService.deleteOwnReactionAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.deleteOwnReaction(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [ChannelService.createReaction]
 */
suspend inline fun ChannelService.createReactionAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.createReaction(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [ChannelService.addPinnedMessage]
 */
suspend inline fun ChannelService.addPinnedMessageAsync(arg0: Long, arg1: Long): Void =
    this.addPinnedMessage(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.deleteChannel]
 */
suspend inline fun ChannelService.deleteChannelAsync(arg0: Long, arg1: String): ChannelResponse =
    this.deleteChannel(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.deleteChannelPermission]
 */
suspend inline fun ChannelService.deleteChannelPermissionAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.deleteChannelPermission(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [ChannelService.deleteMessage]
 */
suspend inline fun ChannelService.deleteMessageAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.deleteMessage(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [ChannelService.bulkDeleteMessages]
 */
suspend inline fun ChannelService.bulkDeleteMessagesAsync(arg0: Long, arg1: BulkDeleteRequest): Void =
    this.bulkDeleteMessages(arg0, arg1).awaitSingle()

/**
 * @see [ChannelService.deleteReaction]
 */
suspend inline fun ChannelService.deleteReactionAsync(
    arg0: Long,
    arg1: Long,
    arg2: String,
    arg3: Long
) {
    this.deleteReaction(arg0, arg1, arg2, arg3).awaitSingle()
}

/**
 * @see [ChannelService.modifyChannel]
 */
suspend inline fun ChannelService.modifyChannelAsync(
    arg0: Long,
    arg1: ChannelModifyRequest,
    arg2: String
): ChannelResponse = this.modifyChannel(arg0, arg1, arg2).awaitSingle()

/**
 * @see [EmojiService.createGuildEmoji]
 */
suspend inline fun EmojiService.createGuildEmojiAsync(
    arg0: Long,
    arg1: GuildEmojiCreateRequest,
    arg2: String
): GuildEmojiResponse = this.createGuildEmoji(arg0, arg1, arg2).awaitSingle()

/**
 * @see [EmojiService.deleteGuildEmoji]
 */
suspend inline fun EmojiService.deleteGuildEmojiAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.deleteGuildEmoji(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [EmojiService.modifyGuildEmoji]
 */
suspend inline fun EmojiService.modifyGuildEmojiAsync(
    arg0: Long,
    arg1: Long,
    arg2: GuildEmojiModifyRequest,
    arg3: String
): GuildEmojiResponse = this.modifyGuildEmoji(arg0, arg1, arg2, arg3).awaitSingle()

/**
 * @see [GuildService.modifyGuildMember]
 */
suspend inline fun GuildService.modifyGuildMemberAsync(
    arg0: Long,
    arg1: Long,
    arg2: GuildMemberModifyRequest,
    arg3: String
) {
    this.modifyGuildMember(arg0, arg1, arg2, arg3).awaitSingle()
}

/**
 * @see [GuildService.createGuildRole]
 */
suspend inline fun GuildService.createGuildRoleAsync(
    arg0: Long,
    arg1: RoleCreateRequest,
    arg2: String
): RoleResponse = this.createGuildRole(arg0, arg1, arg2).awaitSingle()

/**
 * @see [GuildService.modifyGuildRole]
 */
suspend inline fun GuildService.modifyGuildRoleAsync(
    arg0: Long,
    arg1: Long,
    arg2: RoleModifyRequest,
    arg3: String
): RoleResponse = this.modifyGuildRole(arg0, arg1, arg2, arg3).awaitSingle()

/**
 * @see [GuildService.modifyOwnNickname]
 */
suspend inline fun GuildService.modifyOwnNicknameAsync(arg0: Long, arg1: NicknameModifyRequest):
        NicknameModifyResponse = this.modifyOwnNickname(arg0, arg1).awaitSingle()

/**
 * @see [GuildService.addGuildMember]
 */
suspend inline fun GuildService.addGuildMemberAsync(
    arg0: Long,
    arg1: Long,
    arg2: GuildMemberAddRequest
): GuildMemberResponse = this.addGuildMember(arg0, arg1, arg2).awaitSingle()

/**
 * @see [GuildService.createGuildChannel]
 */
suspend inline fun GuildService.createGuildChannelAsync(
    arg0: Long,
    arg1: ChannelCreateRequest,
    arg2: String
): ChannelResponse = this.createGuildChannel(arg0, arg1, arg2).awaitSingle()

/**
 * @see [GuildService.createGuild]
 */
suspend inline fun GuildService.createGuildAsync(arg0: GuildCreateRequest): GuildResponse =
    this.createGuild(arg0).awaitSingle()

/**
 * @see [GuildService.modifyGuildEmbed]
 */
suspend inline fun GuildService.modifyGuildEmbedAsync(arg0: Long, arg1: GuildEmbedModifyRequest):
        GuildEmbedResponse = this.modifyGuildEmbed(arg0, arg1).awaitSingle()

/**
 * @see [GuildService.modifyGuildIntegration]
 */
suspend inline fun GuildService.modifyGuildIntegrationAsync(
    arg0: Long,
    arg1: Long,
    arg2: IntegrationModifyRequest
) {
    this.modifyGuildIntegration(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [GuildService.syncGuildIntegration]
 */
suspend inline fun GuildService.syncGuildIntegrationAsync(arg0: Long, arg1: Long): Void =
    this.syncGuildIntegration(arg0, arg1).awaitSingle()

/**
 * @see [GuildService.modifyGuild]
 */
suspend inline fun GuildService.modifyGuildAsync(
    arg0: Long,
    arg1: GuildModifyRequest,
    arg2: String
): GuildResponse = this.modifyGuild(arg0, arg1, arg2).awaitSingle()

/**
 * @see [GuildService.deleteGuildRole]
 */
suspend inline fun GuildService.deleteGuildRoleAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.deleteGuildRole(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [GuildService.removeGuildBan]
 */
suspend inline fun GuildService.removeGuildBanAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.removeGuildBan(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [GuildService.addGuildMemberRole]
 */
suspend inline fun GuildService.addGuildMemberRoleAsync(
    arg0: Long,
    arg1: Long,
    arg2: Long,
    arg3: String
) {
    this.addGuildMemberRole(arg0, arg1, arg2, arg3).awaitSingle()
}

/**
 * @see [GuildService.deleteGuildIntegration]
 */
suspend inline fun GuildService.deleteGuildIntegrationAsync(arg0: Long, arg1: Long): Void =
    this.deleteGuildIntegration(arg0, arg1).awaitSingle()

/**
 * @see [GuildService.createGuildBan]
 */
suspend inline fun GuildService.createGuildBanAsync(
    arg0: Long,
    arg1: Long,
    arg2: Map<String, Any?>,
    arg3: String
) {
    this.createGuildBan(arg0, arg1, arg2, arg3).awaitSingle()
}

/**
 * @see [GuildService.beginGuildPrune]
 */
suspend inline fun GuildService.beginGuildPruneAsync(
    arg0: Long,
    arg1: Map<String, Any?>,
    arg2: String
): PruneResponse = this.beginGuildPrune(arg0, arg1, arg2).awaitSingle()

/**
 * @see [GuildService.createGuildIntegration]
 */
suspend inline fun GuildService.createGuildIntegrationAsync(
    arg0: Long,
    arg1: IntegrationCreateRequest
): Void = this.createGuildIntegration(
    arg0,
    arg1
).awaitSingle()

/**
 * @see [GuildService.deleteGuild]
 */
suspend inline fun GuildService.deleteGuildAsync(arg0: Long): Void =
    this.deleteGuild(arg0).awaitSingle()

/**
 * @see [GuildService.removeGuildMember]
 */
suspend inline fun GuildService.removeGuildMemberAsync(
    arg0: Long,
    arg1: Long,
    arg2: String
) {
    this.removeGuildMember(arg0, arg1, arg2).awaitSingle()
}

/**
 * @see [GuildService.removeGuildMemberRole]
 */
suspend inline fun GuildService.removeGuildMemberRoleAsync(
    arg0: Long,
    arg1: Long,
    arg2: Long,
    arg3: String
) {
    this.removeGuildMemberRole(arg0, arg1, arg2, arg3).awaitSingle()
}

/**
 * @see [InviteService.deleteInvite]
 */
suspend inline fun InviteService.deleteInviteAsync(arg0: String, arg1: String): InviteResponse =
    this.deleteInvite(arg0, arg1).awaitSingle()

/**
 * @see [UserService.modifyCurrentUser]
 */
suspend inline fun UserService.modifyCurrentUserAsync(arg0: UserModifyRequest): UserResponse =
    this.modifyCurrentUser(arg0).awaitSingle()

/**
 * @see [UserService.leaveGuild]
 */
suspend inline fun UserService.leaveGuildAsync(arg0: Long): Void =
    this.leaveGuild(arg0).awaitSingle()

/**
 * @see [UserService.createDM]
 */
suspend inline fun UserService.createDMAsync(arg0: DMCreateRequest): ChannelResponse =
    this.createDM(arg0).awaitSingle()

/**
 * @see [UserService.createGroupDM]
 */
suspend inline fun UserService.createGroupDMAsync(arg0: GroupDMCreateRequest): ChannelResponse =
    this.createGroupDM(arg0).awaitSingle()

/**
 * @see [WebhookService.createWebhook]
 */
suspend inline fun WebhookService.createWebhookAsync(
    arg0: Long,
    arg1: WebhookCreateRequest,
    arg2: String
): WebhookResponse = this.createWebhook(arg0, arg1, arg2).awaitSingle()

/**
 * @see [WebhookService.modifyWebhook]
 */
suspend inline fun WebhookService.modifyWebhookAsync(
    arg0: Long,
    arg1: WebhookModifyRequest,
    arg2: String
): WebhookResponse = this.modifyWebhook(arg0, arg1, arg2).awaitSingle()

/**
 * @see [WebhookService.deleteWebhook]
 */
suspend inline fun WebhookService.deleteWebhookAsync(arg0: Long, arg1: String): Void =
    this.deleteWebhook(arg0, arg1).awaitSingle()

/**
 * @see [DefaultGatewayClient.close]
 */
suspend inline fun DefaultGatewayClient.closeAsync(arg0: Boolean): Void =
    this.close(arg0).awaitSingle()

/**
 * @see [DefaultGatewayClient.execute]
 */
suspend inline fun DefaultGatewayClient.executeAsync(arg0: String): Void =
    this.execute(arg0).awaitSingle()

/**
 * @see [DefaultGatewayClient.execute]
 */
suspend inline fun DefaultGatewayClient.executeAsync(arg0: String, arg1: GatewayObserver): Void =
    this.execute(arg0, arg1).awaitSingle()

/**
 * @see [DefaultGatewayClient.sendBuffer]
 */
suspend inline fun DefaultGatewayClient.sendBufferAsync(arg0: Publisher<ByteBuf>): Void =
    this.sendBuffer(arg0).awaitSingle()

/**
 * @see [DiscordWebSocketHandler.handle]
 */
suspend inline fun DiscordWebSocketHandler.handleAsync(
    arg0: WebsocketInbound,
    arg1: WebsocketOutbound
) {
    this.handle(arg0, arg1).awaitSingle()
}

/**
 * @see [GatewayClient.execute]
 */
suspend inline fun GatewayClient.executeAsync(arg0: String, arg1: GatewayObserver): Void =
    this.execute(arg0, arg1).awaitSingle()

/**
 * @see [GatewayClient.execute]
 */
suspend inline fun GatewayClient.executeAsync(arg0: String) {
    this.execute(arg0).awaitSingle()
}

/**
 * @see [GatewayClient.sendBuffer]
 */
suspend inline fun GatewayClient.sendBufferAsync(arg0: Publisher<ByteBuf>): Void =
    this.sendBuffer(arg0).awaitSingle()

/**
 * @see [GatewayClient.send]
 */
suspend inline fun GatewayClient.sendAsync(arg0: Publisher<GatewayPayload<*>>) {
    this.send(arg0).awaitSingle()
}

/**
 * @see [GatewayClient.close]
 */
suspend inline fun GatewayClient.closeAsync(arg0: Boolean) {
    this.close(arg0).awaitSingle()
}

/**
 * @see [JacksonPayloadReader.read]
 */
suspend inline fun JacksonPayloadReader.readAsync(arg0: ByteBuf): GatewayPayload<*> =
    this.read(arg0).awaitSingle()

/**
 * @see [JacksonPayloadWriter.write]
 */
suspend inline fun JacksonPayloadWriter.writeAsync(arg0: GatewayPayload<*>): ByteBuf =
    this.write(arg0).awaitSingle()

/**
 * @see [JdkStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> JdkStore<K, V>.saveAsync(arg0: Publisher<Tuple2<K, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [JdkStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> JdkStore<K, V>.saveAsync(
    arg0: K,
    arg1: V
): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [JdkStore.find]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> JdkStore<K, V>.findAsync(arg0: K): V =
    this.find(arg0).awaitSingle()

/**
 * @see [JdkStore.deleteInRange]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> JdkStore<K, V>.deleteInRangeAsync(
    arg0: K,
    arg1: K
): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [JdkStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> JdkStore<K, V>.deleteAsync(arg0: Publisher<K>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [JdkStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> JdkStore<K, V>.deleteAsync(arg0: K): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [NoOpStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> NoOpStore<K, V>.saveAsync(arg0: K, arg1: V): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [NoOpStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> NoOpStore<K, V>.saveAsync(arg0: Publisher<Tuple2<K, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [NoOpStore.find]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> NoOpStore<K, V>.findAsync(arg0: K): V =
    this.find(arg0).awaitSingle()

/**
 * @see [NoOpStore.deleteInRange]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> NoOpStore<K, V>.deleteInRangeAsync(arg0: K, arg1: K): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [NoOpStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> NoOpStore<K, V>.deleteAsync(arg0: K): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [NoOpStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> NoOpStore<K, V>.deleteAsync(arg0: Publisher<K>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [NoOpLongObjStore.find]
 */
suspend inline fun <V : Serializable> NoOpLongObjStore<V>.findAsync(arg0: Long): V =
    this.find(arg0).awaitSingle()

/**
 * @see [NoOpLongObjStore.deleteInRange]
 */
suspend inline fun <V : Serializable> NoOpLongObjStore<V>.deleteInRangeAsync(arg0: Long, arg1: Long): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [NoOpLongObjStore.saveWithLong]
 */
suspend inline fun <V : Serializable> NoOpLongObjStore<V>.saveWithLongAsync(arg0: Publisher<LongObjTuple2<V>>): Void =
    this.saveWithLong(arg0).awaitSingle()

/**
 * @see [NoOpLongObjStore.saveWithLong]
 */
suspend inline fun <V : Serializable> NoOpLongObjStore<V>.saveWithLongAsync(arg0: Long, arg1: V): Void =
    this.saveWithLong(arg0, arg1).awaitSingle()

/**
 * @see [NoOpLongObjStore.delete]
 */
suspend inline fun <V : Serializable> NoOpLongObjStore<V>.deleteAsync(arg0: Long): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [NoOpLongObjStore.delete]
 */
suspend inline fun <V : Serializable> NoOpLongObjStore<V>.deleteAsync(arg0: Publisher<Long>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [ForwardingStore.save]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.saveAsync(arg0: Long, arg1: V): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [ForwardingStore.save]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.saveAsync(arg0: Publisher<Tuple2<Long, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [ForwardingStore.find]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.findAsync(arg0: Long): V =
    this.find(arg0).awaitSingle()

/**
 * @see [ForwardingStore.deleteInRange]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.deleteInRangeAsync(arg0: Long, arg1: Long): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [ForwardingStore.saveWithLong]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.saveWithLongAsync(arg0: Long, arg1: V): Void =
    this.saveWithLong(arg0, arg1).awaitSingle()

/**
 * @see [ForwardingStore.saveWithLong]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.saveWithLongAsync(arg0: Publisher<LongObjTuple2<V>>): Void =
    this.saveWithLong(arg0).awaitSingle()

/**
 * @see [ForwardingStore.delete]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.deleteAsync(arg0: Publisher<Long>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [ForwardingStore.delete]
 */
suspend inline fun <V : Serializable> ForwardingStore<V>.deleteAsync(arg0: Long): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [LongObjStore.save]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.saveAsync(arg0: Long, arg1: V): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [LongObjStore.save]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.saveAsync(arg0: Publisher<Tuple2<Long, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [LongObjStore.find]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.findAsync(arg0: Long): V =
    this.find(arg0).awaitSingle()

/**
 * @see [LongObjStore.deleteInRange]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.deleteInRangeAsync(arg0: Long, arg1: Long): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [LongObjStore.saveWithLong]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.saveWithLongAsync(arg0: Publisher<LongObjTuple2<V>>): Void =
    this.saveWithLong(arg0).awaitSingle()

/**
 * @see [LongObjStore.saveWithLong]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.saveWithLongAsync(arg0: Long, arg1: V): Void =
    this.saveWithLong(arg0, arg1).awaitSingle()

/**
 * @see [LongObjStore.delete]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.deleteAsync(arg0: Publisher<Long>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [LongObjStore.delete]
 */
suspend inline fun <V : Serializable> LongObjStore<V>.deleteAsync(arg0: Long) {
    this.delete(arg0).awaitSingle()
}

/**
 * @see [ReadOnlyLongObjStore.find]
 */
suspend inline fun <V : Serializable> ReadOnlyLongObjStore<V>.findAsync(arg0: Long): V =
    this.find(arg0).awaitSingle()

/**
 * @see [ReadOnlyLongObjStore.deleteInRange]
 */
suspend inline fun <V : Serializable> ReadOnlyLongObjStore<V>.deleteInRangeAsync(arg0: Long, arg1: Long): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [ReadOnlyLongObjStore.saveWithLong]
 */
suspend inline fun <V : Serializable> ReadOnlyLongObjStore<V>.saveWithLongAsync(arg0: Publisher<LongObjTuple2<V>>): Void =
    this.saveWithLong(arg0).awaitSingle()

/**
 * @see [ReadOnlyLongObjStore.saveWithLong]
 */
suspend inline fun <V : Serializable> ReadOnlyLongObjStore<V>.saveWithLongAsync(arg0: Long, arg1: V): Void =
    this.saveWithLong(arg0, arg1).awaitSingle()

/**
 * @see [ReadOnlyLongObjStore.delete]
 */
suspend inline fun <V : Serializable> ReadOnlyLongObjStore<V>.deleteAsync(arg0: Publisher<Long>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [ReadOnlyLongObjStore.delete]
 */
suspend inline fun <V : Serializable> ReadOnlyLongObjStore<V>.deleteAsync(arg0: Long): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [ReadOnlyStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ReadOnlyStore<K, V>.saveAsync(arg0: K, arg1: V): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [ReadOnlyStore.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ReadOnlyStore<K, V>.saveAsync(arg0: Publisher<Tuple2<K, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [ReadOnlyStore.find]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ReadOnlyStore<K, V>.findAsync(arg0: K): V =
    this.find(arg0).awaitSingle()

/**
 * @see [ReadOnlyStore.deleteInRange]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ReadOnlyStore<K, V>.deleteInRangeAsync(
    arg0: K,
    arg1: K
): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [ReadOnlyStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ReadOnlyStore<K, V>.deleteAsync(arg0: K): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [ReadOnlyStore.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> ReadOnlyStore<K, V>.deleteAsync(arg0: Publisher<K>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [Store.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> Store<K, V>.saveAsync(arg0: Publisher<Tuple2<K, V>>): Void =
    this.save(arg0).awaitSingle()

/**
 * @see [Store.save]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> Store<K, V>.saveAsync(arg0: K, arg1: V): Void =
    this.save(arg0, arg1).awaitSingle()

/**
 * @see [Store.find]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> Store<K, V>.findAsync(arg0: K): V =
    this.find(arg0).awaitSingle()

/**
 * @see [Store.deleteInRange]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> Store<K, V>.deleteInRangeAsync(arg0: K, arg1: K): Void =
    this.deleteInRange(arg0, arg1).awaitSingle()

/**
 * @see [Store.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> Store<K, V>.deleteAsync(arg0: Publisher<K>): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [Store.delete]
 */
suspend inline fun <K : Comparable<K>, V : Serializable> Store<K, V>.deleteAsync(arg0: K): Void =
    this.delete(arg0).awaitSingle()

/**
 * @see [DiscordClient.getSelf]
 */
val DiscordClient.selfAsync: Deferred<User>
    inline get() = GlobalScope.async {
        self.awaitSingle()
    }

/**
 * @see [DiscordClient.getApplicationInfo]
 */
val DiscordClient.applicationInfoAsync: Deferred<ApplicationInfo>
    inline get() = GlobalScope.async {
        applicationInfo.awaitSingle()
    }

/**
 * @see [PinsUpdateEvent.getChannel]
 */
val PinsUpdateEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [TypingStartEvent.getUser]
 */
val TypingStartEvent.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [TypingStartEvent.getChannel]
 */
val TypingStartEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [BanEvent.getGuild]
 */
val BanEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [EmojisUpdateEvent.getGuild]
 */
val EmojisUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [IntegrationsUpdateEvent.getGuild]
 */
val IntegrationsUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MemberChunkEvent.getGuild]
 */
val MemberChunkEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MemberJoinEvent.getGuild]
 */
val MemberJoinEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MemberLeaveEvent.getGuild]
 */
val MemberLeaveEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MemberUpdateEvent.getMember]
 */
val MemberUpdateEvent.memberAsync: Deferred<Member>
    inline get() = GlobalScope.async {
        member.awaitSingle()
    }

/**
 * @see [MemberUpdateEvent.getGuild]
 */
val MemberUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [UnbanEvent.getGuild]
 */
val UnbanEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MessageBulkDeleteEvent.getChannel]
 */
val MessageBulkDeleteEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [MessageBulkDeleteEvent.getGuild]
 */
val MessageBulkDeleteEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MessageCreateEvent.getGuild]
 */
val MessageCreateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [MessageDeleteEvent.getChannel]
 */
val MessageDeleteEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [MessageUpdateEvent.getMessage]
 */
val MessageUpdateEvent.messageAsync: Deferred<Message>
    inline get() = GlobalScope.async {
        message.awaitSingle()
    }

/**
 * @see [MessageUpdateEvent.getChannel]
 */
val MessageUpdateEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [MessageUpdateEvent.getGuild]
 */
val MessageUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [ReactionAddEvent.getMessage]
 */
val ReactionAddEvent.messageAsync: Deferred<Message>
    inline get() = GlobalScope.async {
        message.awaitSingle()
    }

/**
 * @see [ReactionAddEvent.getChannel]
 */
val ReactionAddEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [ReactionAddEvent.getUser]
 */
val ReactionAddEvent.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [ReactionAddEvent.getGuild]
 */
val ReactionAddEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [ReactionRemoveAllEvent.getMessage]
 */
val ReactionRemoveAllEvent.messageAsync: Deferred<Message>
    inline get() = GlobalScope.async {
        message.awaitSingle()
    }

/**
 * @see [ReactionRemoveAllEvent.getChannel]
 */
val ReactionRemoveAllEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [ReactionRemoveAllEvent.getGuild]
 */
val ReactionRemoveAllEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [ReactionRemoveEvent.getMessage]
 */
val ReactionRemoveEvent.messageAsync: Deferred<Message>
    inline get() = GlobalScope.async {
        message.awaitSingle()
    }

/**
 * @see [ReactionRemoveEvent.getChannel]
 */
val ReactionRemoveEvent.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [ReactionRemoveEvent.getUser]
 */
val ReactionRemoveEvent.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [ReactionRemoveEvent.getGuild]
 */
val ReactionRemoveEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [PresenceUpdateEvent.getMember]
 */
val PresenceUpdateEvent.memberAsync: Deferred<Member>
    inline get() = GlobalScope.async {
        member.awaitSingle()
    }

/**
 * @see [PresenceUpdateEvent.getUser]
 */
val PresenceUpdateEvent.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [PresenceUpdateEvent.getGuild]
 */
val PresenceUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [RoleCreateEvent.getGuild]
 */
val RoleCreateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [RoleDeleteEvent.getGuild]
 */
val RoleDeleteEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [VoiceServerUpdateEvent.getGuild]
 */
val VoiceServerUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [WebhooksUpdateEvent.getChannel]
 */
val WebhooksUpdateEvent.channelAsync: Deferred<TextChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [WebhooksUpdateEvent.getGuild]
 */
val WebhooksUpdateEvent.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [ApplicationInfo.getOwner]
 */
val ApplicationInfo.ownerAsync: Deferred<User>
    inline get() = GlobalScope.async {
        owner.awaitSingle()
    }

/**
 * @see [Guild.getEmbedChannel]
 */
val Guild.embedChannelAsync: Deferred<GuildChannel>
    inline get() = GlobalScope.async {
        embedChannel.awaitSingle()
    }

/**
 * @see [Guild.getWidgetChannel]
 */
val Guild.widgetChannelAsync: Deferred<GuildChannel>
    inline get() = GlobalScope.async {
        widgetChannel.awaitSingle()
    }

/**
 * @see [Guild.getEveryoneRole]
 */
val Guild.everyoneRoleAsync: Deferred<Role>
    inline get() = GlobalScope.async {
        everyoneRole.awaitSingle()
    }

/**
 * @see [Guild.getAfkChannel]
 */
val Guild.afkChannelAsync: Deferred<VoiceChannel>
    inline get() = GlobalScope.async {
        afkChannel.awaitSingle()
    }

/**
 * @see [Guild.getSystemChannel]
 */
val Guild.systemChannelAsync: Deferred<TextChannel>
    inline get() = GlobalScope.async {
        systemChannel.awaitSingle()
    }

/**
 * @see [Guild.getRegion]
 */
val Guild.regionAsync: Deferred<Region>
    inline get() = GlobalScope.async {
        region.awaitSingle()
    }

/**
 * @see [Guild.getOwner]
 */
val Guild.ownerAsync: Deferred<Member>
    inline get() = GlobalScope.async {
        owner.awaitSingle()
    }

/**
 * @see [GuildChannel.getPosition]
 */
val GuildChannel.positionAsync: Deferred<Int>
    inline get() = GlobalScope.async {
        position.awaitSingle()
    }

/**
 * @see [GuildChannel.getGuild]
 */
val GuildChannel.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [GuildEmoji.getUser]
 */
val GuildEmoji.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [GuildEmoji.getGuild]
 */
val GuildEmoji.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [Member.getHighestRole]
 */
val Member.highestRoleAsync: Deferred<Role>
    inline get() = GlobalScope.async {
        highestRole.awaitSingle()
    }

/**
 * @see [Member.getPresence]
 */
val Member.presenceAsync: Deferred<Presence>
    inline get() = GlobalScope.async {
        presence.awaitSingle()
    }

/**
 * @see [Member.getGuild]
 */
val Member.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [Member.getBasePermissions]
 */
val Member.basePermissionsAsync: Deferred<PermissionSet>
    inline get() = GlobalScope.async {
        basePermissions.awaitSingle()
    }

/**
 * @see [Member.getColor]
 */
val Member.colorAsync: Deferred<Color>
    inline get() = GlobalScope.async {
        color.awaitSingle()
    }

/**
 * @see [Member.getVoiceState]
 */
val Member.voiceStateAsync: Deferred<VoiceState>
    inline get() = GlobalScope.async {
        voiceState.awaitSingle()
    }

/**
 * @see [Message.getAuthorAsMember]
 */
val Message.authorAsMemberAsync: Deferred<Member>
    inline get() = GlobalScope.async {
        authorAsMember.awaitSingle()
    }

/**
 * @see [Message.getWebhook]
 */
val Message.webhookAsync: Deferred<Webhook>
    inline get() = GlobalScope.async {
        webhook.awaitSingle()
    }

/**
 * @see [Message.getGuild]
 */
val Message.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [Message.getChannel]
 */
val Message.channelAsync: Deferred<MessageChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [MessageChannel.getLastMessage]
 */
val MessageChannel.lastMessageAsync: Deferred<Message>
    inline get() = GlobalScope.async {
        lastMessage.awaitSingle()
    }

/**
 * @see [Role.getPosition]
 */
val Role.positionAsync: Deferred<Int>
    inline get() = GlobalScope.async {
        position.awaitSingle()
    }

/**
 * @see [Role.getGuild]
 */
val Role.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [TextChannel.getCategory]
 */
val TextChannel.categoryAsync: Deferred<Category>
    inline get() = GlobalScope.async {
        category.awaitSingle()
    }

/**
 * @see [TextChannel.getLastMessage]
 */
val TextChannel.lastMessageAsync: Deferred<Message>
    inline get() = GlobalScope.async {
        lastMessage.awaitSingle()
    }

/**
 * @see [TextChannel.getGuild]
 */
val TextChannel.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [TextChannel.getPosition]
 */
val TextChannel.positionAsync: Deferred<Int>
    inline get() = GlobalScope.async {
        position.awaitSingle()
    }

/**
 * @see [User.getPrivateChannel]
 */
val User.privateChannelAsync: Deferred<PrivateChannel>
    inline get() = GlobalScope.async {
        privateChannel.awaitSingle()
    }

/**
 * @see [VoiceChannel.getCategory]
 */
val VoiceChannel.categoryAsync: Deferred<Category>
    inline get() = GlobalScope.async {
        category.awaitSingle()
    }

/**
 * @see [Webhook.getChannel]
 */
val Webhook.channelAsync: Deferred<TextChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [Webhook.getCreator]
 */
val Webhook.creatorAsync: Deferred<User>
    inline get() = GlobalScope.async {
        creator.awaitSingle()
    }

/**
 * @see [Webhook.getGuild]
 */
val Webhook.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [ExtendedInvite.getInviter]
 */
val ExtendedInvite.inviterAsync: Deferred<User>
    inline get() = GlobalScope.async {
        inviter.awaitSingle()
    }

/**
 * @see [ExtendedPermissionOverwrite.getRole]
 */
val ExtendedPermissionOverwrite.roleAsync: Deferred<Role>
    inline get() = GlobalScope.async {
        role.awaitSingle()
    }

/**
 * @see [ExtendedPermissionOverwrite.getChannel]
 */
val ExtendedPermissionOverwrite.channelAsync: Deferred<GuildChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [ExtendedPermissionOverwrite.getUser]
 */
val ExtendedPermissionOverwrite.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [ExtendedPermissionOverwrite.getGuild]
 */
val ExtendedPermissionOverwrite.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [Invite.getChannel]
 */
val Invite.channelAsync: Deferred<TextChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [Invite.getGuild]
 */
val Invite.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [Categorizable.getCategory]
 */
val Categorizable.categoryAsync: Deferred<Category>
    inline get() = GlobalScope.async {
        category.awaitSingle()
    }

/**
 * @see [VoiceState.getChannel]
 */
val VoiceState.channelAsync: Deferred<VoiceChannel>
    inline get() = GlobalScope.async {
        channel.awaitSingle()
    }

/**
 * @see [VoiceState.getUser]
 */
val VoiceState.userAsync: Deferred<User>
    inline get() = GlobalScope.async {
        user.awaitSingle()
    }

/**
 * @see [VoiceState.getGuild]
 */
val VoiceState.guildAsync: Deferred<Guild>
    inline get() = GlobalScope.async {
        guild.awaitSingle()
    }

/**
 * @see [ApplicationService.getCurrentApplicationInfo]
 */
val ApplicationService.currentApplicationInfoAsync: Deferred<ApplicationInfoResponse>
    inline get() = GlobalScope.async {
        currentApplicationInfo.awaitSingle()
    }

/**
 * @see [GatewayService.getGateway]
 */
val GatewayService.gatewayAsync: Deferred<GatewayResponse>
    inline get() = GlobalScope.async {
        gateway.awaitSingle()
    }

/**
 * @see [GatewayService.getGatewayBot]
 */
val GatewayService.gatewayBotAsync: Deferred<GatewayResponse>
    inline get() = GlobalScope.async {
        gatewayBot.awaitSingle()
    }

/**
 * @see [UserService.getCurrentUser]
 */
val UserService.currentUserAsync: Deferred<UserResponse>
    inline get() = GlobalScope.async {
        currentUser.awaitSingle()
    }

suspend inline fun DiscordClient.logoutAsync() {
    this.logout().awaitSingle()
}

suspend inline fun DiscordClient.loginAsync() {
    this.login().awaitSingle()
}