package command

import context.GuildCommandContext

object GuildCommandProvider : CommandProvider<GuildCommandContext>() {
    private val commands_ : HashMap<String, CommandInfo> = hashMapOf()

    override var commands: Array<CommandInfo> = commands_.values.toTypedArray()
        private set

    override var modules: Array<ModuleBase<GuildCommandContext>> = arrayOf()
        private set

    override fun find(name: String): CommandInfo? =
            commands_.entries.find { it.key == name }?.value

    override fun addCommand(command: CommandInfo) {
        commands_[command.name] = command
        commands = arrayOf(*commands, command)
    }

    override fun addModule(module: ModuleBase<GuildCommandContext>): GuildCommandProvider = apply {
        modules = arrayOf(*modules, module)
        super.addModule(module)
    }
}
