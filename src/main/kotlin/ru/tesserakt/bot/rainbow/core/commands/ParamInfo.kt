package ru.tesserakt.bot.rainbow.core.commands

import kotlin.reflect.KClass

interface ParamInfo {
    var name : String
    var summary : String
    var isOptional : Boolean
    var type : KClass<*>?
}