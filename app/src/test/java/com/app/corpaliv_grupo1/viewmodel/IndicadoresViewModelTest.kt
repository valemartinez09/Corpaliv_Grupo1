package com.app.corpaliv_grupo1.viewmodel

import com.app.corpaliv_grupo1.data.remote.IndicadoresApi
import com.app.corpaliv_grupo1.data.remote.dto.IndicadorDto
import com.app.corpaliv_grupo1.data.remote.dto.IndicadoresResponse
import com.app.corpaliv_grupo1.model.IndicadoresUiState
import com.app.corpaliv_grupo1.repository.IndicadoresRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class IndicadoresViewModelTest : FunSpec({

    val api = mockk<IndicadoresApi>()

    fun dto(codigo: String, valor: Double) =
        IndicadorDto(codigo, codigo.uppercase(), "Pesos", "2026-11-27T03:00:00.000Z", valor)

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    test("con respuesta válida pasa a Exito con los indicadores") {
        coEvery { api.getIndicadores() } returns IndicadoresResponse(
            null, dto("uf", 39000.0), dto("dolar", 950.0), null, null
        )

        val vm = IndicadoresViewModel(IndicadoresRepository(api))

        val estado = vm.estado.value
        estado.shouldBeInstanceOf<IndicadoresUiState.Exito>()
        estado.indicadores.map { it.codigo } shouldBe listOf("dolar", "uf")
    }

    test("si la API falla pasa a Fallo") {
        coEvery { api.getIndicadores() } throws IOException("sin red")

        val vm = IndicadoresViewModel(IndicadoresRepository(api))

        vm.estado.value shouldBe IndicadoresUiState.Fallo
    }

    test("cargar() permite reintentar después de un fallo") {
        coEvery { api.getIndicadores() } throws IOException("sin red")
        val vm = IndicadoresViewModel(IndicadoresRepository(api))

        coEvery { api.getIndicadores() } returns IndicadoresResponse(
            null, dto("uf", 39000.0), null, null, null
        )
        vm.cargar()

        vm.estado.value.shouldBeInstanceOf<IndicadoresUiState.Exito>()
    }
})