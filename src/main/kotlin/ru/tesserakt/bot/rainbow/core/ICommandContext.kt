package ru.tesserakt.bot.rainbow.core

import sx.blah.discord.handle.obj.IChannel

interface ICommandContext : IContext {
    val message : String
    val channel : IChannel
    val args : Array<String>
}