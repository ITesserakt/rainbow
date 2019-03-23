package command.processors

import command.Hidden
import util.hasAnnotation
import kotlin.reflect.KAnnotatedElement

internal inline class HiddenAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<Boolean> {
    override fun process(): Boolean = elem.hasAnnotation<Hidden>()
}
