package handler

import discord4j.core.event.domain.guild.MemberJoinEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class JoinHandler : Handler<MemberJoinEvent>() {
//    private val map = hashMapOf<Long, Pair<Boolean, Long>>()
//    init {
//        Database.connect { mongoDatabase ->
//            mongoDatabase.getCollection<MemberJoinModule.JoinEvent>()
//                    .find("{*}")
//                    .map { (gId, en, chId) ->
//                        map[gId] = en to chId
//                    }
//        }
//    }

    override fun handle(event: MemberJoinEvent) = GlobalScope.launch {
//        val item = map[event.guildId.asLong()]
//        if (item != null && item.first)
//            event.client.getChannelById(item.second.toSnowflake()).cast<MessageChannel>().subscribe {
//                it.createMessage("https://cdn.discordapp.com/attachments/490951935894093858/547492458892886026/utPRe0e.gif").subscribe()
//            }
    }
}
