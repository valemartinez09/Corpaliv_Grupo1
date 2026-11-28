package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.ProgramasApi
import com.app.corpaliv_grupo1.data.remote.dto.ProgramaResponse
import com.app.corpaliv_grupo1.model.CategoriaPrograma
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException

class ProgramaRepositoryTest : FunSpec({

    val api = mockk<ProgramasApi>()
    val repo = ProgramaRepository(api)

    val programa = ProgramaResponse(
        id = 1,
        titulo = "Programa de inclusión",
        descripcion = "Descripción ficticia",
        categoria = CategoriaPrograma.INCLUSION,
        orden = 1,
        activo = true
    )

    test("listar pide solo los programas activos") {
        coEvery { api.listar(true) } returns listOf(programa)

        val r = repo.listar()

        r.shouldBeInstanceOf<Resultado.Exito<List<ProgramaResponse>>>()
        r.datos shouldBe listOf(programa)
        coVerify { api.listar(true) }
    }

    test("listar con error de red devuelve Resultado.Error") {
        coEvery { api.listar(true) } throws IOException("sin red")

        val r = repo.listar()

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "No se pudo conectar con el servidor"
    }
})