package com.app.corpaliv_grupo1.model

import androidx.annotation.StringRes

enum class EstadoEnvio { EDITANDO, ENVIANDO, ENVIADO }

/** Documento elegido por la persona (ej. carta de presentación). Se guarda su Uri como texto. */
data class ArchivoAdjunto(
    val uri: String,
    val nombre: String,
    val tamanoBytes: Long,
    val tipoMime: String?
)

data class FormularioErrores(
    @StringRes val nombre: Int? = null,
    @StringRes val contacto: Int? = null,
    @StringRes val correo: Int? = null,
    @StringRes val modalidad: Int? = null,
    @StringRes val terminos: Int? = null,
    // El error de la carta no bloquea el envío: la carta es opcional
    @StringRes val carta: Int? = null
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
    val frecuencia: FrecuenciaDonacion? = null,
    val carta: ArchivoAdjunto? = null,
    val mensaje: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: FormularioErrores = FormularioErrores(),
    val envio: EstadoEnvio = EstadoEnvio.EDITANDO
)