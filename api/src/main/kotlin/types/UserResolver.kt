package types

import context.ICommandContext
import discord4j.core.`object`.entity.User
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono
import reactor.util.function.component1
import reactor.util.function.component2
import util.toSnowflake

class UserResolver : MentionableResolver<User>() {
    override fun mentionMatch(context: ICommandContext, input: String): Mono<User> =
            context.client.getUserById(input.substring(2, input.length - 1).toSnowflake())

    override fun idMatch(context: ICommandContext, input: String): Mono<User> =
            context.client.getUserById(input.toSnowflake())

    override fun elseMatch(context: ICommandContext, input: String): Mono<User> =
            context.client.toMono().flatMapMany { it.users }
                    .zipWith(input.split('#').toMono()
                            .filter { it.size == 2 }
                            .switchIfEmpty { throw IllegalStateException("Ожидалось `Name#ID`, получено `$input`") })
                    .filter { (user, input) -> user.username == input[0] && user.discriminator == input[1] }
                    .map { it.t1 }.next()

    override val exceptionMessage: String = "Не найдено ни одного подходящего пользователя"
}