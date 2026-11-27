package com.app.corpaliv_grupo1.data.remote.dto

import com.app.corpaliv_grupo1.model.FrecuenciaDonacion
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador

data class RegistroRequest(
    val tipo: TipoColaborador,
    val nombre: String,
    val contacto: String,
    val correo: String,
    val modalidad: ModalidadColaboracion,
    val frecuencia: FrecuenciaDonacion? = null,
    val mensaje: String,
    val cartaNombre: String? = null
)