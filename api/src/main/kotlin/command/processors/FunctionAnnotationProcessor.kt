package command.processors

import util.Loggers
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KFunction

internal inline class FunctionAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<Boolean> {
    private val logger
        get() = Loggers.getLogger<FunctionAnnotationProcessor>()

    override fun process(): Boolean {
        elem as KFunction<*>
        if (elem.isOperator
                or elem.isInline
                or elem.isInfix
                or elem.isExternal
                or elem.isAbstract
        ) {
            logger.error("", IllegalStateException("Неверный тип функции"))
            return false
        }
        return true
    }
}