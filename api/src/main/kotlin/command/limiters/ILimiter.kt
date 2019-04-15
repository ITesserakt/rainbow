package command.limiters

import context.ICommandContext
import discord4j.core.`object`.entity.Entity
import discord4j.core.`object`.util.Snowflake

interface ILimiter<T : Any> {
    suspend fun checkAccess(context: ICommandContext, needed: List<T>): Boolean
}

abstract class EntityLimiter<T : Entity> : ILimiter<Snowflake> {
    abstract suspend fun getId(context: ICommandContext): Snowflake

    override suspend fun checkAccess(
        context: ICommandContext,
        needed: List<Snowflake>
    ): Boolean = getId(context) in needed
}
