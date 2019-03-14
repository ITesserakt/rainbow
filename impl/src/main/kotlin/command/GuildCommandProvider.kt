package command

import context.GuildCommandContext
import kotlin.reflect.KClass

object GuildCommandProvider : CommandProvider<GuildCommandContext>() {
    override val type: KClass<GuildCommandContext> = GuildCommandContext::class
}
