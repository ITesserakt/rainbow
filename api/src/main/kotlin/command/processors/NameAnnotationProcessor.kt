package command.processors

import command.Command
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.full.findAnnotation

internal inline class NameAnnotationProcessor(override val elem: KAnnotatedElement) : IAnnotationProcessor<String> {
    override fun setAdditionalProcessor(processor: IAnnotationProcessor<*>): NameAnnotationProcessor = apply {
        groupAnnotationProcessor = processor as GroupAnnotationProcessor
    }

    override fun process(): String = elem.findAnnotation<Command>()?.let {
        val name = if (it.name.isNotBlank())
            it.name.replace(' ', '-')
        else {
            elem as KCallable<*>
            elem.name.replace(' ', '-')
        }
        "${groupAnnotationProcessor?.process()}$name"
    } ?: throw IllegalStateException()
}

private var groupAnnotationProcessor: GroupAnnotationProcessor? = null