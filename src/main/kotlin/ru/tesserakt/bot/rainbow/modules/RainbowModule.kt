package ru.tesserakt.bot.rainbow.modules

import org.slf4j.LoggerFactory
import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.*
import ru.tesserakt.bot.rainbow.core.prettyPrint
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.Permissions
import java.awt.Color
import java.util.*
import kotlin.concurrent.fixedRateTimer

class RainbowModule : ModuleBase<CommandContext>() {
    val logger = LoggerFactory.getLogger(RainbowModule::class.java)

    private var stepAccumulator = 0f

    private val colors = arrayOf(Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA)

    private var currentColor = colors[0]
    private var targetColor = colors[1]
    private var index = 2

    private val timerDict = HashMap<String, Timer>()
    private val task: () -> Color = {
        if (stepAccumulator >= 1) {
            stepAccumulator = 0f
            currentColor = targetColor
            targetColor = colors[index]
            index = (index + 1) % 7
        }
        val mixR: Int = (currentColor.red * (1 - stepAccumulator) + targetColor.red * stepAccumulator).toInt()
        val mixG: Int = (currentColor.green * (1 - stepAccumulator) + targetColor.green * stepAccumulator).toInt()
        val mixB: Int = (currentColor.blue * (1 - stepAccumulator) + targetColor.blue * stepAccumulator).toInt()

        stepAccumulator += 0.1f
        Color(mixR, mixG, mixB)
    }

    @Command
    @Summary("Радужный цвет у указанной роли")
    @Restrictions(Permissions.MANAGE_ROLES)
    fun rainbow(role: IRole, delay : Long = 40L) {
        timerDict[role.stringID] = fixedRateTimer(period = delay) {
            runCatching {
                val newColor = task()
                role.changeColor(newColor)
            }.onFailure {
                logger.error(it.prettyPrint())
            }
        }
    }

    @Command
    @Summary("Останавлвает переливание цвета указанной роли")
    @Restrictions(Permissions.MANAGE_ROLES)
    fun rainbow_stop(@Remainder role: IRole) {
        timerDict[role.stringID]?.cancel()
    }
}