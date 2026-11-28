package com.app.corpaliv_grupo1.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val HOST = "http://localhost"

    private const val BASE_REGISTROS = "$HOST:9201/"
    private const val BASE_FUNCIONARIOS = "$HOST:9202/"
    private const val BASE_PROGRAMAS = "$HOST:9203/"

    private fun <T> crear(baseUrl: String, servicio: Class<T>): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(servicio)

    val registros: RegistrosApi by lazy {
        crear(BASE_REGISTROS, RegistrosApi::class.java)
    }

    val funcionarios: FuncionariosApi by lazy {
        crear(BASE_FUNCIONARIOS, FuncionariosApi::class.java)
    }

    val programas: ProgramasApi by lazy {
        crear(BASE_PROGRAMAS, ProgramasApi::class.java)
    }
}