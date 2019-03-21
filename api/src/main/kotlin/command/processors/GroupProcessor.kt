package command.processors

import command.Group
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal class GroupProcessor : IProcessor<String> {
    override fun process(elem: KAnnotatedElement): String = elem.findAnnotation<Group>()?.let {
        "${it.groupName.replace(' ', '_')}_"
    } ?: ""
}