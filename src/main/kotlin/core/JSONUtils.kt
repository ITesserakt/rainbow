package core

import com.google.gson.Gson
import java.io.File

inline fun <reified T> getParsedObject(filePath : String) : T {
    val reader = File(filePath).reader()
    return Gson().fromJson(reader, T::class.java)
}

fun writeToJSON(filePath: String, obj : Any) {
    val output = Gson().toJson(obj)
    File(filePath).writeText(output)
}