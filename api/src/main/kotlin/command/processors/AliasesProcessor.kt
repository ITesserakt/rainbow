package command.processors

import command.Aliases
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal class AliasesProcessor : IProcessor<Array<out String>> {
    override fun process(elem: KAnnotatedElement): Array<out String> =
            elem.findAnnotation<Aliases>()?.aliases ?: emptyArray()
}