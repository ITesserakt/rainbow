package command.processors

import command.RequireDeveloper
import util.hasAnnotation
import kotlin.reflect.KAnnotatedElement

internal inline class RequiredDeveloperAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<Boolean> {
    override fun process(): Boolean = elem.hasAnnotation<RequireDeveloper>()
}
