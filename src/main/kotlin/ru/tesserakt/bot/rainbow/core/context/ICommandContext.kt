package ru.tesserakt.bot.rainbow.core.context

import discord4j.core.DiscordClient
import discord4j.core.`object`.entity.Message

interface ICommandContext {
    val message : Message
    val client : DiscordClient
    val args : List<String>
}