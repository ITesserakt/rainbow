package types

import context.ICommandContext

internal class BooleanResolver : ITypeResolver<Boolean> {
    override suspend fun read(context: ICommandContext, input: String) = input.toBoolean()
}