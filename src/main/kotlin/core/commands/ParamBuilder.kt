package core.commands

import kotlin.reflect.KClass

class ParamBuilder(private val name : String) {

    public val paramInfo = object : ParamInfo {
        override var isOptional: Boolean = false
        override var summary: String = ""
        override var name: String = this@ParamBuilder.name
        override var type : KClass<*>? = null
    }

    fun optional(): ParamBuilder {
        paramInfo.isOptional = true
        return this
    }

    fun addSummary(summary: String): ParamBuilder {
        paramInfo.summary = summary
        return this
    }

    inline fun <reified T> build() : ParamInfo {
        paramInfo.type = T::class
        return paramInfo
    }
}