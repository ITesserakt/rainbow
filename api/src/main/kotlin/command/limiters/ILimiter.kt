package command.limiters

import context.ICommandContext
import discord4j.core.`object`.entity.Entity
import discord4j.core.`object`.util.Snowflake

typealias LimitContext<T> = Pair<ICommandContext, List<T>>

interface ILimiter<T : Any> {
    suspend fun checkAccess(context: LimitContext<T>): Boolean
}

abstract class EntityLimiter<T : Entity> : ILimiter<Snowflake> {
    abstract suspend fun getId(context: ICommandContext): Snowflake

    override suspend fun checkAccess(context: LimitContext<Snowflake>): Boolean =
        getId(context.first) in context.second
}
