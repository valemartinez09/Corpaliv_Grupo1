package com.app.corpaliv_grupo1.viewmodel

import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.data.remote.dto.LoginResponse
import com.app.corpaliv_grupo1.repository.FuncionarioRepository
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
class LoginViewModelTest : FunSpec({

    val repo = mockk<FuncionarioRepository>()

    beforeTest { Dispatchers.setMain(UnconfinedTestDispatcher()) }
    afterTest { Dispatchers.resetMain() }

    test("con campos vacíos muestra errores y no llama al servidor") {
        val vm = LoginViewModel(repo)

        vm.ingresar()

        vm.estado.value.errorUsuario shouldBe R.string.error_usuario
        vm.estado.value.errorClave shouldBe R.string.error_clave
        coVerify(exactly = 0) { repo.login(any(), any()) }
    }

    test("login correcto marca autenticado") {
        coEvery { repo.login("funcionario", "alba2026") } returns
                Resultado.Exito(LoginResponse(1, "funcionario", "Funcionario Demo"))
        val vm = LoginViewModel(repo)
        vm.onUsuarioChange("funcionario")
        vm.onClaveChange("alba2026")

        vm.ingresar()

        vm.estado.value.autenticado shouldBe true
        vm.estado.value.cargando shouldBe false
        vm.estado.value.errorGeneral shouldBe null
    }

    test("credenciales inválidas muestran el mensaje del servidor") {
        coEvery { repo.login(any(), any()) } returns
                Resultado.Error("Credenciales inválidas", 401)
        val vm = LoginViewModel(repo)
        vm.onUsuarioChange("funcionario")
        vm.onClaveChange("mala")

        vm.ingresar()

        vm.estado.value.autenticado shouldBe false
        vm.estado.value.errorGeneral shouldBe "Credenciales inválidas"
    }

    test("escribir en un campo limpia su error") {
        val vm = LoginViewModel(repo)
        vm.ingresar()

        vm.onUsuarioChange("a")

        vm.estado.value.errorUsuario shouldBe null
    }
})