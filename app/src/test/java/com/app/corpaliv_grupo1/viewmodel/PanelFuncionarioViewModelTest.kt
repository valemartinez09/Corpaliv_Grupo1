package com.app.corpaliv_grupo1.viewmodel

import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.model.EstadoRegistro
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.repository.RegistroRemoteRepository
import com.app.corpaliv_grupo1.repository.Resultado
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PanelFuncionarioViewModelTest : FunSpec({

    val repo = mockk<RegistroRemoteRepository>()

    fun registro(id: Long, fecha: Long, estado: EstadoRegistro = EstadoRegistro.PENDIENTE) =
        RegistroResponse(
            id = id,
            tipo = TipoColaborador.PERSONA,
            nombre = "Persona $id",
            contacto = null,
            correo = "p$id@test.cl",
            modalidad = ModalidadColaboracion.DONACION,
            frecuencia = null,
            mensaje = null,
            cartaNombre = null,
            estado = estado,
            fecha = fecha
        )

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    test("al iniciar carga los registros, los más recientes primero") {
        coEvery { repo.listar(any()) } returns
                Resultado.Exito(listOf(registro(1, 100), registro(2, 300), registro(3, 200)))

        val vm = PanelFuncionarioViewModel(repo)

        vm.estado.value.registros.map { it.id } shouldBe listOf(2L, 3L, 1L)
        vm.estado.value.cargando shouldBe false
        vm.estado.value.errorCarga shouldBe false
    }

    test("si falla la carga marca errorCarga") {
        coEvery { repo.listar(any()) } returns Resultado.Error("sin conexión")

        val vm = PanelFuncionarioViewModel(repo)

        vm.estado.value.errorCarga shouldBe true
        vm.estado.value.cargando shouldBe false
        vm.estado.value.registros shouldBe emptyList()
    }

    test("cargar de nuevo se recupera después de un error") {
        coEvery { repo.listar(any()) } returns Resultado.Error("sin conexión")
        val vm = PanelFuncionarioViewModel(repo)

        coEvery { repo.listar(any()) } returns Resultado.Exito(listOf(registro(1, 100)))
        vm.cargar()

        vm.estado.value.errorCarga shouldBe false
        vm.estado.value.registros.size shouldBe 1
    }

    test("marcarContactado reemplaza el registro actualizado") {
        coEvery { repo.listar(any()) } returns Resultado.Exito(listOf(registro(1, 100)))
        coEvery { repo.marcarContactado(1) } returns
                Resultado.Exito(registro(1, 100, EstadoRegistro.CONTACTADO))
        val vm = PanelFuncionarioViewModel(repo)

        vm.marcarContactado(1)

        vm.estado.value.registros.first().estado shouldBe EstadoRegistro.CONTACTADO
        vm.estado.value.procesandoId shouldBe null
        vm.estado.value.errorAccion shouldBe false
    }

    test("si marcarContactado falla deja el registro igual y marca errorAccion") {
        coEvery { repo.listar(any()) } returns Resultado.Exito(listOf(registro(1, 100)))
        coEvery { repo.marcarContactado(1) } returns Resultado.Error("fallo")
        val vm = PanelFuncionarioViewModel(repo)

        vm.marcarContactado(1)

        vm.estado.value.registros.first().estado shouldBe EstadoRegistro.PENDIENTE
        vm.estado.value.errorAccion shouldBe true
        vm.estado.value.procesandoId shouldBe null
    }

    test("eliminar quita el registro de la lista") {
        coEvery { repo.listar(any()) } returns
                Resultado.Exito(listOf(registro(1, 100), registro(2, 200)))
        coEvery { repo.eliminar(1) } returns Resultado.Exito(Unit)
        val vm = PanelFuncionarioViewModel(repo)

        vm.eliminar(1)

        vm.estado.value.registros.map { it.id } shouldBe listOf(2L)
        coVerify { repo.eliminar(1) }
    }

    test("si eliminar falla conserva la lista y marca errorAccion") {
        coEvery { repo.listar(any()) } returns Resultado.Exito(listOf(registro(1, 100)))
        coEvery { repo.eliminar(1) } returns Resultado.Error("fallo")
        val vm = PanelFuncionarioViewModel(repo)

        vm.eliminar(1)

        vm.estado.value.registros.size shouldBe 1
        vm.estado.value.errorAccion shouldBe true
    }

    test("descartarErrorAccion limpia el error") {
        coEvery { repo.listar(any()) } returns Resultado.Exito(listOf(registro(1, 100)))
        coEvery { repo.eliminar(1) } returns Resultado.Error("fallo")
        val vm = PanelFuncionarioViewModel(repo)
        vm.eliminar(1)

        vm.descartarErrorAccion()

        vm.estado.value.errorAccion shouldBe false
    }
})