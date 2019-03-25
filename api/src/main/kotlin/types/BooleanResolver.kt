package types

import context.ICommandContext

class BooleanResolver : ITypeResolver<Boolean> {
    override suspend fun read(context: ICommandContext, input: String) = input.toBoolean()
}