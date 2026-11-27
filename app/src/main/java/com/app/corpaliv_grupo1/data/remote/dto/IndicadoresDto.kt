package com.app.corpaliv_grupo1.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IndicadorDto(
    val codigo: String?,
    val nombre: String?,
    @SerializedName("unidad_medida") val unidadMedida: String?,
    val fecha: String?,
    val valor: Double?
)

data class IndicadoresResponse(
    val fecha: String?,
    val uf: IndicadorDto?,
    val dolar: IndicadorDto?,
    val euro: IndicadorDto?,
    val utm: IndicadorDto?
)