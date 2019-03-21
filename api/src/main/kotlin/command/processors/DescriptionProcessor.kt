package command.processors

import command.Summary
import util.Loggers
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaMethod

class DescriptionProcessor : IProcessor<String> {
    private val logger = Loggers.getLogger<DescriptionProcessor>()

    override fun process(elem: KAnnotatedElement): String = elem.findAnnotation<Summary>()?.let {
        if (it.description.isBlank() && elem is KCallable<*>)
            logger.warn("Нет описания для команды `${elem.name}`")
        it.description
    } ?: {
        elem as KFunction<*>
        logger.warn("Нет описания для команды `${elem.javaMethod?.declaringClass?.canonicalName}:${elem.name}`")
        "Описание отсутствует"
    }()
}