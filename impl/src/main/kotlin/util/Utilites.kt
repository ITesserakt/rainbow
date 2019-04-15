package util

import discord4j.core.DiscordClient
import java.time.LocalTime

private lateinit var startedTime_: LocalTime

var DiscordClient.startedTime: LocalTime
    get() = startedTime_
    set(value) {
        startedTime_ = value
    }