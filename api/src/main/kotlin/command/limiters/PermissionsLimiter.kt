package command.limiters

import context.GuildCommandContext
import discord4j.core.`object`.util.Permission
import util.await

class PermissionsLimiter : ILimiter<Permission> {
    override suspend fun checkAccess(context: LimitContext<Permission>): Boolean {
        val (context, needed) = context
        return (context as GuildCommandContext).author
            .asMember(context.guildId)
            .flatMap { it.basePermissions }.await()
            .containsAll(needed)
    }
}