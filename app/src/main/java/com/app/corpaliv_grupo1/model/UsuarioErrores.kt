package com.app.corpaliv_grupo1.model

// Guarda el posible error de cada campo (null = sin error)
data class UsuarioErrores(
    val nombre: String? = null,
    val correo: String? = null,
    val clave: String? = null,
    val direccion: String? = null
)