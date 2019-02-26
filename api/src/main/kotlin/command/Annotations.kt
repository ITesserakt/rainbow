package command

import discord4j.core.`object`.util.Permission

@Target(AnnotationTarget.FUNCTION)
annotation class Command (val name: String = "")

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class Summary (val description: String = "")

@Target(AnnotationTarget.FUNCTION)
annotation class Permissions(vararg val permissions : Permission)

@Deprecated("Not implemented")
@Target(AnnotationTarget.FUNCTION)
annotation class Hide