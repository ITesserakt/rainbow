package command

import context.PrivateChannelCommandContext

/**
 * A provider for private commands
 */
object PrivateChannelCommandProvider : CommandProvider<PrivateChannelCommandContext>(PrivateChannelCommandContext::class)
