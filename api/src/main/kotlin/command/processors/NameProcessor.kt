package command.processors

import command.Command
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

internal class NameProcessor(private val type: KClass<*>) : IProcessor<String> {
    override fun process(elem: KAnnotatedElement): String = elem.findAnnotation<Command>()?.let {
        val name = if (it.name.isNotBlank())
            it.name.replace(' ', '-')
        else {
            elem as KCallable<*>
            elem.name.replace(' ', '-')
        }
        "${GroupProcessor().process(type)}$name"
    } ?: throw IllegalStateException()
}