package ru.tesserakt.bot.rainbow.core

import sx.blah.discord.api.IDiscordClient
import sx.blah.discord.handle.obj.IGuild
import sx.blah.discord.handle.obj.IUser

interface IContext {
    val user : IUser
    val client : IDiscordClient
    var guild : IGuild
    val contextId : Long
}
