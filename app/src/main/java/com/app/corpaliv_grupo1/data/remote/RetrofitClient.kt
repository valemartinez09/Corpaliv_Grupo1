package com.app.corpaliv_grupo1.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 10.0.2.2 = el PC visto desde el emulador de Android Studio.
    // Si pruebas en un celular físico, cambia esta IP por la IP local de tu PC.
    private const val HOST = "http://10.0.2.2"

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