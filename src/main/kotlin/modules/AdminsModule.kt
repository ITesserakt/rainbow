package modules

import CURRENTDIR
import core.ModuleBase
import core.commands.CommandContext
import core.commands.CommandService
import core.types.ResolverService
import sx.blah.discord.handle.obj.IUser
import java.io.File

internal class AdminsModule : ModuleBase<CommandContext>() {
    init {
        CommandService.addCommand {
            name = "ban"
            action = {
                val user = ResolverService.getForType<IUser>().read(it, it.args[0])
                val reason = if (it.args.size > 1) it.args[1] else ""
                val file = File("$CURRENTDIR/resources/omae_wa_mou.gif")

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