package modules

import com.sun.javafx.util.Utils
import command.*
import context.GuildCommandContext
import discord4j.core.`object`.entity.Role
import discord4j.core.`object`.util.Permission
import discord4j.core.`object`.util.Snowflake
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import util.NoPermissionsException
import util.RandomColor
import util.await
import util.toOptional
import java.awt.Color
import java.time.Duration

@Group("rainbow")
class RainbowModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    private val rainbows = hashMapOf<Snowflake, Job>()

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

    @Command("start")
    @Permissions(Permission.MANAGE_ROLES)
    @Summary("Радужное переливание цвета роли")
    suspend fun rainbowStart(role: Role, `delay in ms`: Long = 500, step: Float = 0.5f) {
        val clampedDelay = clampDelay(`delay in ms`)
        val clampedStep = Utils.clamp(0.01f, step, 0.9f)

        checkForRightRolePosition(role)

        rainbows[role.id] = scope.launch {
            while (isActive) {
                val color = task(clampedStep)
                delay(clampedDelay.toMillis())

                role.edit { it.setColor(color) }.await()
            }
        }
    }

    private fun clampDelay(`delay in ms`: Long): Duration =
        Duration.ofMillis(`delay in ms`)
            .takeIf { it >= Duration.ofMillis(100) }.toOptional()
            .orElseThrow { IllegalArgumentException("Слишком маленькая задержка ( < 100 )") }

    private suspend fun checkForRightRolePosition(role: Role) {
        val botAsMember = context.client.self.flatMap { it.asMember(context.guildId) }.await()
        if (!botAsMember.hasHigherRoles(listOf(role)).await())
            throw NoPermissionsException(
                """Роль `${role.name}` находится иерархически выше роли бота.
                  |В разделе управления сервером передвиньте роль бота выше необходимой роли""".trimMargin()
            )
    }

    @Command("stop")
    @Summary("Остановка переливания роли")
    @Permissions(Permission.MANAGE_ROLES)
    fun rainbowStop(@Continuous role: Role) {
        rainbows.remove(role.id)?.cancel()
    }
}