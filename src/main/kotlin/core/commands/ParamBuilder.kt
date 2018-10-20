package core.commands

import kotlin.reflect.KClass

class ParamBuilder {
    val paramInfo = object : ParamInfo {
        override var isOptional: Boolean = false
        override var summary: String = ""
        override var name: String = ""
        override var type : KClass<*>? = null
    }

    var name = ""
        set(value) {
            paramInfo.name = value
        }

    var isOptional = false
        set(value) {
            paramInfo.isOptional = value
        }

    var summary = ""
        set(value) {
            paramInfo.summary = value
        }

    inline fun <reified T> build() : ParamInfo {
        paramInfo.type = T::class
        return paramInfo
    }
}