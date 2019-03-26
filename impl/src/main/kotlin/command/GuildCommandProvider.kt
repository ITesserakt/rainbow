package command

import context.GuildCommandContext

/**
 * A provider for guild commands
 */
object GuildCommandProvider : CommandProvider<GuildCommandContext>(GuildCommandContext::class)
