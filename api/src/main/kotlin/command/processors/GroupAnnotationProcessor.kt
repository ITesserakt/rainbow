package command.processors

import command.Group
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal inline class GroupAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<String> {
    override fun process(): String = elem.findAnnotation<Group>()?.let {
        "${it.groupName.replace('_', ' ')} "
    } ?: ""
}