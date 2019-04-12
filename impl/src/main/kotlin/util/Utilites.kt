package util

import discord4j.core.DiscordClient
import java.time.LocalTime

private lateinit var startedTime: LocalTime

var DiscordClient.startedTime: LocalTime
    get() = util.startedTime
    set(value) {
        util.startedTime = value
    }