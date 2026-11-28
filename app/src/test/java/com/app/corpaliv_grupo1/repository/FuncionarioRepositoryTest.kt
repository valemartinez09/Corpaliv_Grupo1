package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.FuncionariosApi
import com.app.corpaliv_grupo1.data.remote.dto.LoginRequest
import com.app.corpaliv_grupo1.data.remote.dto.LoginResponse
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.io.IOException

class FuncionarioRepositoryTest : FunSpec({

    val api = mockk<FuncionariosApi>()
    val repo = FuncionarioRepository(api)

    test("login quita los espacios del usuario antes de enviarlo") {
        coEvery { api.login(LoginRequest("funcionario", "clave")) } returns
                LoginResponse(1, "funcionario", "Funcionario Demo")

        val r = repo.login("  funcionario  ", "clave")

        r.shouldBeInstanceOf<Resultado.Exito<LoginResponse>>()
        r.datos.usuario shouldBe "funcionario"
        coVerify { api.login(LoginRequest("funcionario", "clave")) }
    }

    test("login no modifica la clave") {
        coEvery { api.login(any()) } returns LoginResponse(1, "u", "n")

        repo.login("u", " clave con espacios ")

        coVerify { api.login(LoginRequest("u", " clave con espacios ")) }
    }

    test("login con el servidor caído devuelve error de conexión") {
        coEvery { api.login(any()) } throws IOException("timeout")

        val r = repo.login("u", "c")

        r.shouldBeInstanceOf<Resultado.Error>()
        r.mensaje shouldBe "No se pudo conectar con el servidor"
    }
})