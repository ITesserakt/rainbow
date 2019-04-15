@file:Suppress("UNCHECKED_CAST")

import com.google.common.reflect.ClassPath
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import reactor.core.publisher.Mono
import java.io.File
import kotlin.reflect.KFunction
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.valueParameters
import kotlin.streams.toList

fun main() {
    @Suppress("UnstableApiUsage")
    val classes = ClassPath.from(Thread.currentThread().contextClassLoader)
        .getTopLevelClassesRecursive("discord4j")
        .map { it.load().kotlin }
    val funcs = classes.stream()
        .flatMap { it.declaredMemberFunctions.stream() }
        .filter { it.visibility == KVisibility.PUBLIC }
        .filter { it.returnType.classifier == Mono::class }
        .filter { it.name.startsWith("get") && it.valueParameters.isNotEmpty() }
        .map { it as KFunction<Mono<*>> }
        .toList()

    val file = FileSpec.builder("", "Discord4K get additions")
        .addImport("kotlinx.coroutines.reactive", "awaitFirstOrNull", "awaitSingle")
        .addImport("kotlinx.coroutines", "GlobalScope", "async")
        .addAnnotation(
            AnnotationSpec.builder(Suppress::class)
                .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
                .addMember("\"unused\"")
                .build()
        )

    MonoFunBuilder(file)
        .build(funcs)

    val t = file.build()
    t.writeTo(File("/home/tesserakt/rainbow/generated/src/main/kotlin/"))
}