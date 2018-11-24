package ru.tesserakt.bot.rainbow.core.commands

import sx.blah.discord.handle.obj.Permissions

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Remainder

@Target(AnnotationTarget.FUNCTION)
annotation class Command(val name : String = "")

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.VALUE_PARAMETER)
annotation class Summary(val description : String)

@Target(AnnotationTarget.FUNCTION)
annotation class Aliases(vararg val alias : String)

@Target(AnnotationTarget.FUNCTION)
annotation class Restrictions(vararg val permissions : Permissions)