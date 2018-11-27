package ru.tesserakt.bot.rainbow.core

import com.alibaba.fastjson.JSON
import java.io.File

inline fun <reified T> getParsedObject(name : String) : T {
    val text = getResource(name).readText()
    return JSON.parseObject(text, T::class.java)
}

fun writeToJSON(name: String, obj : Any) {
    val output = JSON.toJSONString(obj)
    File(getResource(name).toURI()).writeText(output)
}