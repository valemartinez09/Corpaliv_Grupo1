package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.ProgramasApi
import com.app.corpaliv_grupo1.data.remote.RetrofitClient
import com.app.corpaliv_grupo1.data.remote.dto.ProgramaResponse

class ProgramaRepository(
    private val api: ProgramasApi = RetrofitClient.programas
) {
    suspend fun listar(): Resultado<List<ProgramaResponse>> =
        llamarApi { api.listar() }
}