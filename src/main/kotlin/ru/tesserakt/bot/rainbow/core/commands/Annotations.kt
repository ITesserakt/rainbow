package ru.tesserakt.bot.rainbow.core.commands

import discord4j.core.`object`.util.Permission

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Remainder

@Target(AnnotationTarget.FUNCTION)
annotation class CommandAnn(val name : String = "")

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class Summary(val description : String)

@Target(AnnotationTarget.FUNCTION)
annotation class Aliases(vararg val aliases : String)

@Target(AnnotationTarget.FUNCTION)
annotation class Permissions(vararg val permissions : Permission)

@Target(AnnotationTarget.FUNCTION)
annotation class RequireLogin