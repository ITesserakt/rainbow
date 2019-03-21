package command.processors

import util.Loggers
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KFunction

class FunctionProcessor : IProcessor<Boolean> {
    private val logger = Loggers.getLogger<FunctionProcessor>()

    override fun process(elem: KAnnotatedElement): Boolean {
        elem as KFunction<*>
        if (elem.isOperator
                or elem.isInline
                or elem.isInfix
                or elem.isExternal
                or elem.isSuspend
                or elem.isAbstract
        ) {
            logger.error("", IllegalStateException("Неверный тип функции"))
            return false
        }
        return true
    }
}