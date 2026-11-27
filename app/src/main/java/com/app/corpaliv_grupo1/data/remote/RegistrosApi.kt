package com.app.corpaliv_grupo1.data.remote

import com.app.corpaliv_grupo1.data.remote.dto.EstadoRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroRequest
import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.model.EstadoRegistro
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface RegistrosApi {

    @GET("api/registros")
    suspend fun listar(
        @Query("estado") estado: EstadoRegistro? = null
    ): List<RegistroResponse>

    @GET("api/registros/{id}")
    suspend fun obtener(@Path("id") id: Long): RegistroResponse

    @POST("api/registros")
    suspend fun crear(@Body datos: RegistroRequest): RegistroResponse

    @PUT("api/registros/{id}")
    suspend fun actualizar(
        @Path("id") id: Long,
        @Body datos: RegistroRequest
    ): RegistroResponse

    @PATCH("api/registros/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Long,
        @Body datos: EstadoRequest
    ): RegistroResponse

    @DELETE("api/registros/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}