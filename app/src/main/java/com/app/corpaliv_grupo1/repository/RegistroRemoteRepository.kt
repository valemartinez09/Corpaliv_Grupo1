package com.app.corpaliv_grupo1.repository


import com.app.corpaliv_grupo1.data.remote.RegistrosApi
import com.app.corpaliv_grupo1.data.remote.RetrofitClient
import com.app.corpaliv_grupo1.data.remote.dto.EstadoRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.model.EstadoRegistro

class RegistroRemoteRepository(
    private val api: RegistrosApi = RetrofitClient.registros
) {

    suspend fun listar(estado: EstadoRegistro? = null): Resultado<List<RegistroResponse>> =
        llamarApi { api.listar(estado) }

    suspend fun obtener(id: Long): Resultado<RegistroResponse> =
        llamarApi { api.obtener(id) }

    suspend fun crear(datos: RegistroRequest): Resultado<RegistroResponse> =
        llamarApi { api.crear(datos) }

    suspend fun actualizar(id: Long, datos: RegistroRequest): Resultado<RegistroResponse> =
        llamarApi { api.actualizar(id, datos) }

    suspend fun marcarContactado(id: Long): Resultado<RegistroResponse> =
        llamarApi { api.cambiarEstado(id, EstadoRequest(EstadoRegistro.CONTACTADO)) }

    suspend fun eliminar(id: Long): Resultado<Unit> =
        llamarApi { api.eliminar(id) }
}