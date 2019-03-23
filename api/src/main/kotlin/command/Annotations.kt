package command

import discord4j.core.`object`.util.Permission

@Target(AnnotationTarget.FUNCTION)
annotation class Command(val name: String = "")

@Target(AnnotationTarget.FUNCTION)
annotation class Summary(val description: String = "")

@Target(AnnotationTarget.FUNCTION)
annotation class Permissions(vararg val permissions: Permission)

@Target(AnnotationTarget.FUNCTION)
annotation class Hidden

@Target(AnnotationTarget.FUNCTION)
annotation class Aliases(vararg val aliases: String)

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Continuous

@Target(AnnotationTarget.CLASS)
annotation class Group(val groupName: String)

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class RequireDeveloper