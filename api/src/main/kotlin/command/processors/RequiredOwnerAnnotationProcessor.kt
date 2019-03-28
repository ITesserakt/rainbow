package command.processors

import command.CommandInfo
import command.RequireOwner
import context.ICommandContext
import util.hasAnnotation
import kotlin.reflect.KAnnotatedElement

internal inline class RequiredOwnerAnnotationProcessor(override val elem: KAnnotatedElement) :
    IAnnotationProcessor<Boolean> {
    override fun process(): Boolean =
        elem.hasAnnotation<RequireOwner>()
}

interface RequiredOwnerProcessor {
    suspend fun processOwner(command: CommandInfo, context: ICommandContext)
}