package command.processors

import command.Summary
import util.Loggers
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.javaMethod

internal inline class DescriptionAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<String> {
    private val logger
        get() = Loggers.getLogger<DescriptionAnnotationProcessor>()

    override fun process(): String = elem.findAnnotation<Summary>()?.let {
        if (it.description.isBlank() && elem is KCallable<*>)
            logger.warn("Нет описания для команды `${elem.name}`")
        it.description
    } ?: {
        elem as? KFunction<*> ?: throw IllegalArgumentException("Может запускаться только для функций")
        logger.warn("Нет описания для команды `${elem.javaMethod?.declaringClass?.canonicalName}:${elem.name}`")
        "Описание отсутствует"
    }()
}