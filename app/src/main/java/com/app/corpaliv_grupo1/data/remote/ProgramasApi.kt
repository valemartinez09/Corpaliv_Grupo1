package com.app.corpaliv_grupo1.data.remote

import com.app.corpaliv_grupo1.data.remote.dto.ProgramaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProgramasApi {

    @GET("api/programas")
    suspend fun listar(
        @Query("soloActivos") soloActivos: Boolean = true
    ): List<ProgramaResponse>
}