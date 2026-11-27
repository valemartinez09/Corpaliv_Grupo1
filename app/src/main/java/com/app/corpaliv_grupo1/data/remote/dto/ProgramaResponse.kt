package com.app.corpaliv_grupo1.data.remote.dto

import com.app.corpaliv_grupo1.model.CategoriaPrograma

data class ProgramaResponse(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val categoria: CategoriaPrograma,
    val orden: Int,
    val activo: Boolean
)