package command

import context.ICommandContext
import org.reflections.Reflections
import util.Loggers
import util.getSubTypesOf

/**
 * Loader that finds commands in [pathToPackage]
 */
class CommandLoader(private vararg val pathToPackage: String) {
    private val logger = Loggers.getLogger<CommandLoader>()

    /**
     * Find and loads commands with specified [registry] from [pathToPackage]
     */
    fun load(registry: CommandRegistry) {
        var count = 0
        logger.info("Начата загрузка модулей...")

        Reflections(pathToPackage)
            .getSubTypesOf<ModuleBase<ICommandContext>>()
            .forEach {
                registry.register(it.kotlin)
                count++
            }.run { logger.info("Загружено $count модулей") }
    }
}