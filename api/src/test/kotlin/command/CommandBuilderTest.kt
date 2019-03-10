package command

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CommandBuilderTest {
//    private val action = { x : Int -> x * 2 }
//    @MockK
//    private lateinit var module : ModuleBase<*>
//
//    @Test
//    fun `create normal command by CommandBuilder`() {
//        val command = CommandBuilder.createNew()
//                .setName("multiply by 2")
//                .setFuncPointer(action::reflect)
//                .setModulePointer(module)
//                .setDescription("test")
//                .setParams(::action.parameters.toTypedArray())
//                .setPermissions(PermissionSet.none())
//                .build()
//
//        assertEquals(command.name, "multiply by 2")
//        assertEquals(command.functionPointer, action::reflect)
//        assertEquals(command.modulePointer, module)
//        assertEquals(command.description, "test")
//        assertEquals(command.parameters.size, ::action.parameters.toTypedArray().size)
//        assertEquals(command.permissions, PermissionSet.none())
//    }
}