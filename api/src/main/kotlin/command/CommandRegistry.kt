package command

import command.processors.*
import context.ICommandContext
import util.hasAnnotation
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.declaredMemberFunctions

object CommandRegistry {
    private val providers = mutableListOf<CommandProvider<out ICommandContext>>()

    fun register(instance: KClass<ModuleBase<ICommandContext>>) {
        val instanceOfInstance = instance.createInstance()

        instance.declaredMemberFunctions
                .filter { it.hasAnnotation<Command>() }
            .filter { FunctionAnnotationProcessor(it).process() }
                .forEach { func ->
                    val provider = providers.find { it.type == instanceOfInstance.contextType }
                            ?: throw NoSuchElementException("Нет подходящего провайдера для контекста ${instanceOfInstance.contextType.simpleName}")

                    provider.addCommand(
                        CommandInfo(
                            name = NameAnnotationProcessor(func)
                                .setAdditionalProcessor(GroupAnnotationProcessor(instance))
                                .process(),
                            description = DescriptionAnnotationProcessor(func)
                                .process(),
                            functionPointer = func,
                            modulePointer = instanceOfInstance,
                            aliases = AliasesAnnotationProcessor(func)
                                .process(),
                            permissions = PermissionsAnnotationProcessor(func)
                                .process(),
                            isHidden = HiddenAnnotationProcessor(func)
                                .process(),
                            isRequiringDeveloper = RequiredDeveloperAnnotationProcessor(func)
                                .process()
                        )
                    )
                }
    }

    fun addProvider(provider: CommandProvider<out ICommandContext>): CommandRegistry {
        providers.add(provider)
        return this
    }
}
