package command.limiters

import context.ICommandContext
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Snowflake
import util.await

open class MemberLimiter : EntityLimiter<Member>() {
    override suspend fun getId(context: ICommandContext): Snowflake =
        context.message.authorAsMember.await().id
}