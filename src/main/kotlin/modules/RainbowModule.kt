package modules

import core.ModuleBase
import core.commands.CommandService
import core.types.ResolverService
import sx.blah.discord.handle.obj.IRole
import java.awt.Color
import java.util.*
import kotlin.concurrent.timer

class RainbowModule : ModuleBase() {
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

    init {
        CommandService.addCommand {
            name = "rainbow"
            action = {
                val role = ResolverService.getForType<IRole>().read(it, it.args[0])
                val delta = if (it.args.size > 1) ResolverService.getForType<Float>().read(it, it.args[1]) else .1f
                if (delta < .06) throw IllegalArgumentException("Задержка не можеть быть меньше 0.06с")

                start(role, (delta * 1000).toLong())
            }
            parameters({ name = "role"; build<IRole>() }, { name = "delta time in sec"; isOptional = true; build<Float>() })
            summary = "Радужный цвет у указанной роли"
            build()
        }

        CommandService.addCommand {
            name = "rainbow_stop"
            action = {
                val role = ResolverService.getForType<IRole>().read(it, it.args[0])
                stop(role)
            }
            summary = "Останавлвает перелиание цвета указанной роли"
            parameters({ name = "role";build<IRole>() })
            build()
        }
    }

    private fun start(role: IRole, delta: Long) {
        timerDict[role.stringID] = timer(period = delta) {
            val color = task()
            role.changeColor(color)
        }
    }

    private fun stop(role: IRole) {
        timerDict[role.stringID]?.cancel()
    }
}