package com.app.corpaliv_grupo1.model

import androidx.annotation.StringRes

enum class EstadoEnvio { EDITANDO, ENVIANDO, ENVIADO }

data class FormularioErrores(
    @StringRes val nombre: Int? = null,
    @StringRes val contacto: Int? = null,
    @StringRes val correo: Int? = null,
    @StringRes val modalidad: Int? = null,
    @StringRes val terminos: Int? = null
) {
    fun sinErrores(): Boolean =
        listOfNotNull(nombre, contacto, correo, modalidad, terminos).isEmpty()
}

data class FormularioUiState(
    val tipo: TipoColaborador = TipoColaborador.PERSONA,
    val nombre: String = "",
    val contacto: String = "",
    val correo: String = "",
    val modalidad: ModalidadColaboracion? = null,
    val mensaje: String = "",
    val frecuencia: FrecuenciaDonacion? = null,   // <- nueva
    val aceptaTerminos: Boolean = false,
    val errores: FormularioErrores = FormularioErrores(),
    val envio: EstadoEnvio = EstadoEnvio.EDITANDO,
    val errorGuardado: Boolean = false   // true si falló el guardado en la base
)