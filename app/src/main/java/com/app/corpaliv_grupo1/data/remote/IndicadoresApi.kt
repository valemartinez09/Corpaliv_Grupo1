package com.app.corpaliv_grupo1.data.remote

import com.app.corpaliv_grupo1.data.remote.dto.IndicadoresResponse
import retrofit2.http.GET

interface IndicadoresApi {

    // GET https://mindicador.cl/api
    @GET("api")
    suspend fun getIndicadores(): IndicadoresResponse
}