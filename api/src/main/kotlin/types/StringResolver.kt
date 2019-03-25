package types

import context.ICommandContext

class StringResolver : ITypeResolver<String> {
    override suspend fun read(context: ICommandContext, input: String): String = input
}

