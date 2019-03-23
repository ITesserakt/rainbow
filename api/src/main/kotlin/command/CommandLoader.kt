package command

import context.ICommandContext
import util.Loggers
import java.io.File

class CommandLoader(private val pathToPackage: String) {
    private val logger = Loggers.getLogger<CommandLoader>()

    private fun find(): List<Class<*>> {
        val buffer = mutableListOf<Class<*>>()
        val path = pathToPackage.replace(PKG_SEPARATOR, DIR_SEPARATOR)
        val url = Thread.currentThread().contextClassLoader.getResource(path)
                ?: throw IllegalArgumentException("Неверно введено имя пакета")
        val file = File(url.toURI())
        file.listFiles().forEach {
            buffer.addAll(findRec(it, pathToPackage))
        }
        return buffer
    }

    private fun findRec(file: File, parentPKG: String): List<Class<*>> {
        val buffer = mutableListOf<Class<*>>()
        val resource = "$parentPKG$PKG_SEPARATOR${file.name}"
        if (file.isDirectory)
            file.listFiles().map {
                buffer.addAll(findRec(it, resource))
            }
        else if (resource.endsWith(CLASS_FILE_SUFFIX) && '$' !in resource) {
            val className = resource.dropLast(CLASS_FILE_SUFFIX.length)
            buffer.add(Class.forName(className))
        }
        return buffer
    }

    fun load() {
        var count = 0
        logger.info("Начата загрузка модулей...")

        find().filterIsInstance<Class<ModuleBase<ICommandContext>>>()
                .forEach {
                    CommandRegistry.register(it.kotlin)
                    count++
                }.run { logger.info("Загружено $count модулей") }
    }

    private companion object Constants {
        private const val PKG_SEPARATOR = '.'
        private val DIR_SEPARATOR = File.pathSeparatorChar
        private const val CLASS_FILE_SUFFIX = ".class"
    }
}