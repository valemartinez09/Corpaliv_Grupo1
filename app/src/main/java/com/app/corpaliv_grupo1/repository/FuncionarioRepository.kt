package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.FuncionariosApi
import com.app.corpaliv_grupo1.data.remote.RetrofitClient
import com.app.corpaliv_grupo1.data.remote.dto.LoginRequest
import com.app.corpaliv_grupo1.data.remote.dto.LoginResponse

class FuncionarioRepository(
    private val api: FuncionariosApi = RetrofitClient.funcionarios
) {
    suspend fun login(usuario: String, clave: String): Resultado<LoginResponse> =
        llamarApi { api.login(LoginRequest(usuario.trim(), clave)) }
}