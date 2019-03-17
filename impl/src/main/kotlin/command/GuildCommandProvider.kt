package command

import context.GuildCommandContext

object GuildCommandProvider : CommandProvider<GuildCommandContext>(GuildCommandContext::class)
