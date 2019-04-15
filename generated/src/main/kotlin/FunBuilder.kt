import com.squareup.kotlinpoet.*
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.valueParameters
import kotlin.reflect.full.withNullability
import kotlin.reflect.jvm.jvmErasure

class MonoFunBuilder(override val fileSpec: FileSpec.Builder) : CallableBuilder<CallableMono> {
    override fun build(list: List<CallableMono>): FileSpec.Builder {
        list.forEach {
            val parameters = it.valueParameters
            val declaringClass = it.instanceParameter!!.type
            val statement = "return this.${it.name}(${parameters.joinToString { p -> p.name!! }}).awaitFirstOrNull()"
            val returnTypeWithoutMono = it.returnType.arguments[0].type!!.withNullability(true).asTypeName()

            fileSpec.addFunction(FunSpec.builder("${it.name}Async")
                .addKdoc("@see [${declaringClass.jvmErasure.simpleName}.${it.name}]")
                .returns(returnTypeWithoutMono)
                .receiver(declaringClass.jvmErasure)
                .addModifiers(KModifier.INLINE, KModifier.SUSPEND)
                .addStatement(statement)
                .addTypeVariables(declaringClass.arguments.map { a -> TypeVariableName.invoke(a.type.toString()) })
                .addParameters(
                    parameters.map { p ->
                        ParameterSpec.builder(
                            p.name!!,
                            p.type.jvmErasure
                        ).build()
                    }
                ).build()
            )
        }
        return fileSpec
    }
}