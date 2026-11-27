package com.app.corpaliv_grupo1.data.remote.dto

data class ApiError(
    val estado: Int,
    val mensaje: String?,
    val detalles: Map<String, String>?
)