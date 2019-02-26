package modules

//import command.Command
//import command.ModuleBase
//import command.Permissions
//import context.GuildCommandContext
//import discord4j.core.`object`.entity.MessageChannel
//import discord4j.core.`object`.util.Permission
//import org.litote.kmongo.eq
//import org.litote.kmongo.findOne
//import org.litote.kmongo.getCollection
//import util.Database
//
//class MemberJoinModule : ModuleBase<GuildCommandContext>() {
//    @Command
//    @Permissions(Permission.ADMINISTRATOR)
//    fun joinMessages(enabled: Boolean, channel: MessageChannel) = Database.connect { mongoDatabase ->
//        val collection = mongoDatabase.getCollection<JoinEvent>()
//        collection.findOne(JoinEvent::guildId eq context.guildId.asLong())
//                ?: collection.insertOne(JoinEvent(context.guildId.asLong(), enabled, context.message.channelId.asLong()))
//    }
//
//    internal data class JoinEvent(internal val guildId: Long, internal val enabled: Boolean, internal val channelId: Long)
//}