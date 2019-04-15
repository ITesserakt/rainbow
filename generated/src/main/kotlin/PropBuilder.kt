import com.squareup.kotlinpoet.*
import kotlinx.coroutines.Deferred
import kotlin.reflect.KTypeProjection
import kotlin.reflect.full.createType
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.jvmErasure

class MonoPropBuilder(override val fileSpec: FileSpec.Builder) : CallableBuilder<CallableMono> {
    override fun build(list: List<CallableMono>): FileSpec.Builder {
        list.forEach {
            val declaringClass = it.instanceParameter!!.type
            val statement = """return GlobalScope.async {
                |     ${it.name}.awaitFirstOrNull()
                |}
            """.trimMargin()
            val returnType = Deferred::class.createType(
                listOf(KTypeProjection.invariant(it.returnType.arguments[0].type!!.withNullability(true)))
            )

            fileSpec.addProperty(
                PropertySpec.builder("${it.name.drop(3)}Async", returnType.asTypeName())
                    .mutable(false)
                    .addKdoc("@see [${declaringClass.jvmErasure.simpleName}.${it.name}]")
                    .receiver(declaringClass.jvmErasure)
                    .addTypeVariables(declaringClass.arguments.map { a -> TypeVariableName.Companion.invoke(a.type.toString()) })
                    .getter(
                        FunSpec.getterBuilder()
                            .addStatement(statement)
                            .addModifiers(KModifier.INLINE)
                            .build()
                    )
                    .build()
            )
        }
        return fileSpec
    }
}