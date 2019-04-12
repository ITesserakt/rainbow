package command.limiters

import context.ICommandContext
import discord4j.core.`object`.entity.User
import discord4j.core.`object`.util.Snowflake

open class UserLimiter : EntityLimiter<User>() {
    override suspend fun getId(context: ICommandContext): Snowflake = context.author.id
}