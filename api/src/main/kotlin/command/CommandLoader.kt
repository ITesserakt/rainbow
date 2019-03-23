package command

import context.ICommandContext
import org.reflections.Reflections
import util.Loggers
import util.getSubTypesOf

class CommandLoader(private val pathToPackage: String) {
    private val logger = Loggers.getLogger<CommandLoader>()

    fun load() {
        var count = 0
        logger.info("Начата загрузка модулей...")

        Reflections(pathToPackage)
            .getSubTypesOf<ModuleBase<ICommandContext>>()
            .forEach {
                CommandRegistry.register(it.kotlin)
                count++
            }.run { logger.info("Загружено $count модулей") }
    }
}