package ru.tesserakt.bot.rainbow.core

import com.alibaba.fastjson.JSON
import java.io.File

inline fun <reified T> getParsedObject(filePath : String) : T {
    val text = File(filePath).reader().readText()
    return JSON.parseObject(text, T::class.java)
}

fun writeToJSON(filePath: String, obj : Any) {
    val output = JSON.toJSONString(obj)
    File(filePath).writeText(output)
}