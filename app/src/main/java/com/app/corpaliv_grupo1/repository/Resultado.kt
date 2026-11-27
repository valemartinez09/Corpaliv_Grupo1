package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.dto.ApiError
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

sealed class Resultado<out T> {
    data class Exito<T>(val datos: T) : Resultado<T>()
    data class Error(
        val mensaje: String,
        val codigo: Int? = null,
        val detalles: Map<String, String> = emptyMap()
    ) : Resultado<Nothing>()
}

// Ejecuta una llamada a la API y convierte las fallas en Resultado.Error
suspend fun <T> llamarApi(bloque: suspend () -> T): Resultado<T> =
    try {
        Resultado.Exito(bloque())
    } catch (e: HttpException) {
        val error = runCatching {
            Gson().fromJson(e.response()?.errorBody()?.string(), ApiError::class.java)
        }.getOrNull()
        Resultado.Error(
            mensaje = error?.mensaje ?: "Error del servidor (${e.code()})",
            codigo = e.code(),
            detalles = error?.detalles ?: emptyMap()
        )
    } catch (e: IOException) {
        android.util.Log.e("API", "Fallo de red: ${e.javaClass.simpleName} - ${e.message}", e)
        Resultado.Error("No se pudo conectar con el servidor")
    }