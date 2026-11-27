package com.app.corpaliv_grupo1.repository

import com.app.corpaliv_grupo1.data.remote.IndicadoresApi
import com.app.corpaliv_grupo1.data.remote.IndicadoresRetrofit
import com.app.corpaliv_grupo1.data.remote.dto.IndicadorDto
import com.app.corpaliv_grupo1.model.Indicador
import kotlin.coroutines.cancellation.CancellationException

class IndicadoresRepository(
    private val api: IndicadoresApi = IndicadoresRetrofit.api
) {

    suspend fun obtenerIndicadores(): Result<List<Indicador>> {
        return try {
            val respuesta = api.getIndicadores()
            val lista = listOfNotNull(
                respuesta.dolar?.toModelo(),
                respuesta.uf?.toModelo(),
                respuesta.euro?.toModelo()
            )
            if (lista.isEmpty()) {
                Result.failure(IllegalStateException("La API no devolvió indicadores"))
            } else {
                Result.success(lista)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun IndicadorDto.toModelo(): Indicador? {
        val valor = valor ?: return null
        return Indicador(
            codigo = codigo ?: return null,
            nombre = nombre ?: codigo,
            valor = valor,
            fecha = fecha?.take(10) ?: ""
        )
    }
}