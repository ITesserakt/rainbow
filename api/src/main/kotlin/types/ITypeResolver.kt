package types

import context.ICommandContext

/**
 * Represents parser from [String] to [T]
 */
interface ITypeResolver <T> {
    suspend fun read(context: ICommandContext, input: String): T

    suspend fun readToEnd(context: ICommandContext, input: List<String>): T {
        val stringArgs = input.joinToString(" ")
        return read(context, stringArgs)
    }
}