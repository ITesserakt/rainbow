package command.processors

import kotlin.reflect.KAnnotatedElement

internal interface IAnnotationProcessor<T> {
    val elem: KAnnotatedElement
    fun process(): T
    fun setAdditionalProcessor(processor: IAnnotationProcessor<*>): IAnnotationProcessor<T> = this
}