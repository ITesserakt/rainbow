package command.processors

import command.RequireOwner
import util.hasAnnotation
import kotlin.reflect.KAnnotatedElement

internal inline class RequiredOwnerAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<Boolean> {
    override fun process(): Boolean =
        elem.hasAnnotation<RequireOwner>()
}