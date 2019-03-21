package modules

import command.*
import context.GuildCommandContext
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.Snowflake
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty
import reactor.core.publisher.toMono
import reactor.util.function.component1
import reactor.util.function.component2
import util.NoPermissionsException
import util.RandomColor
import util.toOptional
import java.awt.Color
import java.time.Duration

@Group("rainbow")
class RainbowModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    private val rainbows = hashMapOf<Snowflake, Disposable>()

    private var stepAccumulator = 0f
    private var currentColor = RandomColor
    private var targetColor = RandomColor

    private val task: (Float) -> Color = {
        if (stepAccumulator >= 1) {
            stepAccumulator = 0f

            currentColor = targetColor
            targetColor = RandomColor
        }
        val mixR: Int = (currentColor.red * (1 - stepAccumulator) + targetColor.red * stepAccumulator).toInt()
        val mixG: Int = (currentColor.green * (1 - stepAccumulator) + targetColor.green * stepAccumulator).toInt()
        val mixB: Int = (currentColor.blue * (1 - stepAccumulator) + targetColor.blue * stepAccumulator).toInt()

        stepAccumulator += it
        Color(mixR, mixG, mixB)
    }

    @Command
    @Permissions(Permission.MANAGE_ROLES)
    @Summary("Радужное переливание цвета роли")
    fun start(role: Role, `delay in ms`: Long = 500, step: Float = 0.5f) {
        val delay = Duration.ofMillis(`delay in ms`)
                .takeIf { it >= Duration.ofMillis(100) }.toOptional()
                .orElseThrow { IllegalArgumentException("Слишком маленькая задержка ( < 100 )") }

        context.client.self
                .flatMap { it.asMember(context.guildId) }
                .filterWhen { it.hasHigherRoles(listOf(role)) }
                .switchIfEmpty {
                    throw NoPermissionsException(
                            """Роль `${role.name}` находится иерархически выше роли бота.
                              |В разделе управления сервером передвиньте роль бота выше необходимой роли""".trimMargin()
                    )
                }.subscribe()

        rainbows[role.id] = Mono.fromCallable { task(step) }
                .delayElement(delay)
                .zipWith(role.toMono())
                .flatMap { (color, role) ->
                    role.edit {
                        it.setColor(color)
                    }
                }.repeat { !rainbows[role.id]?.isDisposed!! }
                .onBackpressureDrop()
                .subscribe()
    }

    @Command("stop")
    @Summary("Остановка переливания роли")
    fun rainbowStop(@Continuous role: Role) {
        if (rainbows.contains(role.id)) rainbows.remove(role.id)?.dispose()
    }
}