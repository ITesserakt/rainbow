package ru.tesserakt.bot.rainbow.modules

import ru.tesserakt.bot.rainbow.core.ModuleBase
import ru.tesserakt.bot.rainbow.core.commands.Command
import ru.tesserakt.bot.rainbow.core.commands.CommandContext
import ru.tesserakt.bot.rainbow.core.commands.Restrictions
import ru.tesserakt.bot.rainbow.core.commands.Summary
import sx.blah.discord.handle.obj.IRole
import sx.blah.discord.handle.obj.Permissions
import java.awt.Color
import java.util.*
import kotlin.concurrent.fixedRateTimer

class RainbowModule : ModuleBase<CommandContext>() {
    private var stepAccumulator = 0f
    private val rndObj = Random()
    private val random: () -> Int = { rndObj.nextInt(256) }

    private var currentColor = Color(random(), random(), random())
    private var targetColor = Color(random(), random(), random())

    private val timerDict = HashMap<String, Timer>()
    private val task: () -> Color = {
        if (stepAccumulator >= 1) {
            stepAccumulator = 0f
            val r = random()
            val g = random()
            val b = random()
            currentColor = targetColor
            targetColor = Color(r, g, b)
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
    fun rainbow(role: IRole, delay : Long = 40) {
        timerDict[role.stringID] = fixedRateTimer(period = delay) {
            val color = task()
            role.changeColor(color)
        }
    }

    @Command
    @Summary("Останавлвает перелиание цвета указанной роли")
    @Restrictions(Permissions.MANAGE_ROLES)
    fun rainbow_stop(role: IRole) {
        timerDict[role.stringID]?.cancel()
    }
}