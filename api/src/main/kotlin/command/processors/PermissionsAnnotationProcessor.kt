package command.processors

import command.CommandInfo
import command.Permissions
import context.ICommandContext
import discord4j.core.`object`.util.PermissionSet
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

internal inline class PermissionsAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<PermissionSet> {
    override fun process(): PermissionSet = PermissionSet.of(
        elem.findAnnotation<Permissions>()?.permissions
            .orEmpty()
            .map { it.value }
            .ifEmpty { listOf(0L) }
            .reduce(Long::or)
    )
}

interface PermissionsProcessor {
    suspend fun processPerms(command: CommandInfo, context: ICommandContext)
}