package modules

import command.*
import context.GuildCommandContext
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.Snowflake
import reactor.core.Disposable
import reactor.core.publisher.Mono
import reactor.util.function.component1
import reactor.util.function.component2
import util.RandomColor
import java.awt.Color
import java.time.Duration
import javax.naming.NoPermissionException

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
    @Summary("Радужное переливание роли")
    fun rainbow(role: Role, `delay in ms`: Long = 500, step: Float = 0.5f) {
        val delay = Duration.ofMillis(`delay in ms`)
        if (delay < Duration.ofMillis(100)) {
            context.reply("Слишком маленькая задержка ( < 100 )")
            return
        }

        context.client.self
                .flatMap { it.asMember(context.guildId) }
                .flatMapMany { it.roles }.flatMap { it.position }.defaultIfEmpty(-1).last()
                .zipWith(role.position)
                .filter { (p1, p2) -> p1 < p2 }.then()
                .subscribe {
                    throw NoPermissionException(
                            """Роль `${role.name}` находится иерархически выше роли бота.
                               В разделе управления сервером передвиньте роль бота выше необходимой роли""".trimIndent()
                    )
                }

        rainbows[role.id] = Mono.fromCallable { task(step) }
                .delayElement(delay).zipWith(context.guild.flatMap { it.getRoleById(role.id) })
                .doOnNext { (color, role) ->
                    role.edit {
                        it.setColor(color)
                    }.subscribe()
                }.repeat { !rainbows[role.id]?.isDisposed!! }
                .onBackpressureDrop()
                .subscribe()
    }

    @Command("rainbow_stop")
    @Summary("Остановка переливания роли")
    fun rainbowStop(@Continuous role: Role) {
        if (rainbows.contains(role.id)) rainbows.remove(role.id)?.dispose()
    }
}