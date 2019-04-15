package command.limiters

import authorAsMemberAsync
import basePermissionsAsync
import context.GuildCommandContext
import context.ICommandContext
import discord4j.core.`object`.util.Permission

class PermissionsLimiter : ILimiter<Permission> {
    override suspend fun checkAccess(
        context: ICommandContext,
        needed: List<Permission>
    ): Boolean {
        return (context as GuildCommandContext).message
            .authorAsMemberAsync.await()
            .basePermissionsAsync.await()
            .containsAll(needed)
    }
}