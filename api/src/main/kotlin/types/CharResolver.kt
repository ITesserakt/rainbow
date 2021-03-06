package types

import context.ICommandContext

internal class CharResolver : ITypeResolver<Char> {
    override suspend fun read(context: ICommandContext, input: String): Char = input[0]
}