package command

import discord4j.core.`object`.util.Permission

/**
 * Defines a function that can be ran from discord
 * @param name name of command. If empty, using function name
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Command(val name: String = "")

/**
 * Adds a description to command
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Summary(val description: String)

/**
 * Specify which role can run this command
 * @param permissions an [Array] of permissions
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Permissions(vararg val permissions: Permission)

/**
 * Hides command from output of the [CommandProvider.commands]
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
annotation class Hidden

/**
 * Adds aliases through which the command can be run
 * @param aliases an [Array] of aliases
 */
@Target(AnnotationTarget.FUNCTION)
annotation class Aliases(vararg val aliases: String)

/**
 * Allows you to pass arguments including spaces. Must be on the last argument
 *
 * ```kotlin
 *     fun test(@Continuous message : String)
 * ```
 * !test I love kotlin -> message = I love kotlin
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Continuous

/**
 * Adds a [groupName] prefix to all commands in module
 */
@Target(AnnotationTarget.CLASS)
annotation class Group(val groupName: String)

/**
 * The command can only be run by the developer
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class RequireDeveloper

/**
 * The command can only be run by the owner of the guild
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class RequireOwner