package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.IndicadoresApi
import com.app.corpaliv_grupo1.data.remote.dto.IndicadorDto
import com.app.corpaliv_grupo1.data.remote.dto.IndicadoresResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import java.io.IOException

class IndicadoresRepositoryTest : FunSpec({

    val api = mockk<IndicadoresApi>()
    val repo = IndicadoresRepository(api)

    fun dto(codigo: String, valor: Double?, fecha: String? = "2026-11-27T03:00:00.000Z") =
        IndicadorDto(codigo, codigo.uppercase(), "Pesos", fecha, valor)

    test("entrega dólar, UF y euro en ese orden") {
        coEvery { api.getIndicadores() } returns IndicadoresResponse(
            fecha = null,
            uf = dto("uf", 39000.0),
            dolar = dto("dolar", 950.5),
            euro = dto("euro", 1030.0),
            utm = dto("utm", 68000.0)
        )

        val r = repo.obtenerIndicadores()

        r.isSuccess shouldBe true
        r.getOrNull()!!.map { it.codigo } shouldBe listOf("dolar", "uf", "euro")
    }

    test("recorta la fecha a los primeros 10 caracteres") {
        coEvery { api.getIndicadores() } returns IndicadoresResponse(
            null, dto("uf", 39000.0), null, null, null
        )

        val r = repo.obtenerIndicadores()

        r.getOrNull()!!.first().fecha shouldBe "2026-11-27"
    }

    test("ignora indicadores sin valor") {
        coEvery { api.getIndicadores() } returns IndicadoresResponse(
            null, dto("uf", null), dto("dolar", 950.0), null, null
        )

        val r = repo.obtenerIndicadores()

        r.getOrNull()!!.map { it.codigo } shouldBe listOf("dolar")
    }

    test("si la API no trae indicadores devuelve falla") {
        coEvery { api.getIndicadores() } returns IndicadoresResponse(null, null, null, null, null)

        val r = repo.obtenerIndicadores()

        r.isFailure shouldBe true
        r.exceptionOrNull().shouldBeInstanceOf<IllegalStateException>()
    }

    test("si hay error de red devuelve falla con la excepción") {
        coEvery { api.getIndicadores() } throws IOException("sin red")

        val r = repo.obtenerIndicadores()

        r.isFailure shouldBe true
        r.exceptionOrNull().shouldBeInstanceOf<IOException>()
    }
})