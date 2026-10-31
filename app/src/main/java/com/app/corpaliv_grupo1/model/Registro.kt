package com.app.corpaliv_grupo1.model

data class Registro(
    val id: Long = 0,
    val tipo: TipoColaborador,
    val nombre: String,
    val contacto: String,
    val correo: String,
    val modalidad: ModalidadColaboracion,
    val frecuencia: FrecuenciaDonacion? = null,
    val mensaje: String,
    val fecha: Long = System.currentTimeMillis()
)