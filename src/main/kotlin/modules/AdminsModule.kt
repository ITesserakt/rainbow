package modules

import core.ModuleBase
import core.RESOURCES
import core.commands.CommandService
import core.types.ResolverService
import sx.blah.discord.handle.obj.IUser
import sx.blah.discord.handle.obj.Permissions
import java.io.File

internal class AdminsModule : ModuleBase() {
    init {
        CommandService.addCommand {
            name = "ban"
            action = {
                val user = ResolverService.parse<IUser>(it, 0)
                val file = File("$RESOURCES/omae_wa_mou.gif")
                val reason = when {
                    it.args.size > 1 -> ResolverService.parse(it, 1, true)
                    else -> ""
                }

                it.guild.banUser(user, reason)
                it.replyFile(file)
            }
            restrictions(Permissions.BAN)
            summary = "Банит указанного пользователя по указанной причине"
            parameters({ name = "user"; build<IUser>() }, { name = "reason"; isOptional = true; build<String>() })
            build()
        }

        CommandService.addCommand {
            name = "kick"
            action = {
                val user = ResolverService.parse<IUser>(it, 0, true)
                it.guild.kickUser(user)
            }
            restrictions(Permissions.KICK)
            summary = "Кикает указанного пользователя за плохое поведение"
            parameters({ name = "user"; build<IUser>() })
            build()
        }

        CommandService.addCommand {
            name = "unban"
            action = {
                val userId = ResolverService.parse<Long>(it, 0, true)
                it.guild.pardonUser(userId)
            }
            restrictions(Permissions.BAN)
            summary = "Разбанивает пользователя"
            parameters({ name = "user"; build<String>() })
            build()
        }
    }
}