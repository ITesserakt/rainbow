package modules

import core.ModuleBase
import core.RESOURCES
import core.commands.CommandService
import core.types.ResolverService
import sx.blah.discord.handle.obj.IUser
import java.io.File

internal class AdminsModule : ModuleBase() {
    init {
        CommandService.addCommand {
            name = "ban"
            action = {
                val user = ResolverService.getForType<IUser>().read(it, it.args[0])
                val file = File("$RESOURCES/omae_wa_mou.gif")
                val reason = when {
                    it.args.size > 1 -> ResolverService.getForType<String>().read(it, it.args[1])
                    else -> ""
                }

                it.guild.banUser(user, reason)
                it.replyFile(file)
            }
            summary = "Банит указанного пользователя по указанной причине"
            parameters({ name = "user"; build<IUser>() }, { name = "reason"; isOptional = true; build<String>() })
            build()
        }

        CommandService.addCommand {
            name = "kick"
            action = {
                val user = ResolverService.getForType<IUser>().read(it, it.args[0])
                it.guild.kickUser(user)
            }
            summary = "Кикает указанного пользователя за плохое поведение"
            parameters({ name = "user"; build<IUser>() })
            build()
        }

        CommandService.addCommand {
            name = "unban"
            action = {
                val userId = ResolverService.getForType<Long>().read(it, it.args[0])
                it.guild.pardonUser(userId)
            }
            summary = "Разбанивает пользователя"
            parameters({ name = "user"; build<String>() })
            build()
        }
    }
}