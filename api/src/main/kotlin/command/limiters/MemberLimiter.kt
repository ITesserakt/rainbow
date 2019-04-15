package command.limiters

import authorAsMemberAsync
import context.ICommandContext
import discord4j.core.`object`.entity.Member
import discord4j.core.`object`.util.Snowflake

open class MemberLimiter : EntityLimiter<Member>() {
    override suspend fun getId(context: ICommandContext): Snowflake =
        context.message.authorAsMemberAsync.await().id
}