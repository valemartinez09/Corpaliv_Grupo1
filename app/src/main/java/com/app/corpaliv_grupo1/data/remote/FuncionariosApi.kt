package com.app.corpaliv_grupo1.data.remote

import com.app.corpaliv_grupo1.data.remote.dto.LoginRequest
import com.app.corpaliv_grupo1.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FuncionariosApi {
    @POST("api/funcionarios/login")
    suspend fun login(@Body datos: LoginRequest): LoginResponse
}