package modules

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
import java.awt.Color
import kotlin.collections.set

@Group("rainbow")
class RainbowModule : ModuleBase<GuildCommandContext>(GuildCommandContext::class) {
    private val rainbows = hashMapOf<Snowflake, Job>()

    private var stepAccumulator = 0f
    private var currentColor = RandomColor
    private var targetColor = RandomColor

    private fun task(step: Float): Color {
        if (stepAccumulator >= 1) {
            stepAccumulator = 0f

            currentColor = targetColor
            targetColor = RandomColor
        }
        val mixR: Int = (currentColor.red * (1 - stepAccumulator) + targetColor.red * stepAccumulator).toInt()
        val mixG: Int = (currentColor.green * (1 - stepAccumulator) + targetColor.green * stepAccumulator).toInt()
        val mixB: Int = (currentColor.blue * (1 - stepAccumulator) + targetColor.blue * stepAccumulator).toInt()

        stepAccumulator += step
        return Color(mixR, mixG, mixB)
    }

    @Command("start")
    @Permissions(Permission.MANAGE_ROLES)
    @Summary("Радужное переливание цвета роли")
    suspend fun rainbowStart(role: Role, `delay in sec`: Long = 30, step: Float = 0.5f) {
        val clampedDelay = `delay in sec`.coerceAtLeast(10) * 1000
        val clampedStep = step.coerceIn(0.1f..0.9f)

        checkForRightRolePosition(role)

        rainbows[role.id] = scope.launch {
            while (isActive) {
                val color = task(clampedStep)
                delay(clampedDelay)

                role.edit { it.setColor(color) }.await()
            }
        }
    }

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