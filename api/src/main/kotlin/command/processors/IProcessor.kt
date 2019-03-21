package command.processors

import kotlin.reflect.KAnnotatedElement

internal interface IProcessor<T> {
    fun process(elem: KAnnotatedElement): T
}