package modules

import core.ICommandContext
import core.ModuleBase
import core.console.ConsoleService
import core.types.ResolverService
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

class ConsoleModule : ModuleBase() {
    private lateinit var guild: IGuild

    init {
        ConsoleService.addCommand {
            name = "quit"
            summary = "Выход"
            action = {
                System.exit(0)
            }
            aliases = arrayOf("exit")
            build()
        }

        ConsoleService.addCommand {
            name = "login"
            action = {
                guild = ResolverService.parse(it, 0, true)
                it.guild = guild
            }
            summary = "Дает доступ к командной строке"
            parameters({ name = "guildId"; build<IGuild>() })
            build()
        }

        ConsoleService.addCommand {
            name = "shaperize"
            summary = "Дает самую высокую роль, доступную боту"
            action = {
                updateLateInitProps(it)

                val user = ResolverService.parse<IUser>(it, 0, true)
                val botRolePos = it.user.getRolesForGuild(it.guild).maxBy { role -> role.position }?.position
                val maxBotRole = it.guild.roles
                        .first {role -> botRolePos?.minus(1) == role.position && !role.isEveryoneRole}

                user.addRole(maxBotRole)
            }
            parameters({ name = "user"; build<IUser>() })
            build()
        }
    }

    private fun updateLateInitProps(context: ICommandContext) {
        if (::guild.isInitialized) {
            context.guild = guild
        } else throw IllegalArgumentException("'Login' first!")
    }
}