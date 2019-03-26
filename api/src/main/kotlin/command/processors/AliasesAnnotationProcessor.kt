package command.processors

import command.Aliases
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal inline class AliasesAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<List<String>> {
    override fun process(): List<String> =
        elem.findAnnotation<Aliases>()?.aliases?.toList().orEmpty()
}