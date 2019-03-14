package command

import context.PrivateChannelCommandContext
import kotlin.reflect.KClass

object PrivateChannelCommandProvider : CommandProvider<PrivateChannelCommandContext>() {
    override val type: KClass<PrivateChannelCommandContext> = PrivateChannelCommandContext::class
}
