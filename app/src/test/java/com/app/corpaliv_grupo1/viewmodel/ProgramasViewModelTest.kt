package com.app.corpaliv_grupo1.viewmodel

import com.app.corpaliv_grupo1.data.remote.dto.ProgramaResponse
import com.app.corpaliv_grupo1.model.CategoriaPrograma
import com.app.corpaliv_grupo1.repository.ProgramaRepository
import com.app.corpaliv_grupo1.repository.Resultado
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ProgramasViewModelTest : FunSpec({

    val repo = mockk<ProgramaRepository>()

    fun programa(id: Long, orden: Int) = ProgramaResponse(
        id = id,
        titulo = "Programa $id",
        descripcion = "Descripción ficticia",
        categoria = CategoriaPrograma.INCLUSION,
        orden = orden,
        activo = true
    )

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    test("carga los programas ordenados por orden") {
        coEvery { repo.listar() } returns
                Resultado.Exito(listOf(programa(1, 3), programa(2, 1), programa(3, 2)))

        val vm = ProgramasViewModel(repo)

        vm.estado.value.programas.map { it.id } shouldBe listOf(2L, 3L, 1L)
        vm.estado.value.cargando shouldBe false
        vm.estado.value.error shouldBe null
    }

    test("si falla muestra el mensaje de error") {
        coEvery { repo.listar() } returns Resultado.Error("No se pudo conectar con el servidor")

        val vm = ProgramasViewModel(repo)

        vm.estado.value.error shouldBe "No se pudo conectar con el servidor"
        vm.estado.value.cargando shouldBe false
        vm.estado.value.programas shouldBe emptyList()
    }

    test("reintentar con cargar() limpia el error") {
        coEvery { repo.listar() } returns Resultado.Error("fallo")
        val vm = ProgramasViewModel(repo)

        coEvery { repo.listar() } returns Resultado.Exito(listOf(programa(1, 1)))
        vm.cargar()

        vm.estado.value.error shouldBe null
        vm.estado.value.programas.size shouldBe 1
    }
})