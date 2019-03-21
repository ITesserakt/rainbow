package command.processors

import command.Permissions
import discord4j.core.`object`.util.Permission
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal class PermissionsProcessor : IProcessor<Array<out Permission>> {
    override fun process(elem: KAnnotatedElement): Array<out Permission> =
            elem.findAnnotation<Permissions>()?.permissions ?: emptyArray()
}