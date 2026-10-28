package com.app.corpaliv_grupo1.model

import androidx.annotation.StringRes
import com.app.corpaliv_grupo1.R

enum class ModalidadColaboracion(@StringRes val etiqueta: Int) {
    DONACION(R.string.mod_donacion),
    ALIANZA(R.string.mod_alianza),
    VOLUNTARIADO(R.string.mod_voluntariado),
    COMPRA(R.string.mod_compra),
    CAPACITACION(R.string.mod_capacitacion)
}

enum class TipoColaborador(val modalidades: List<ModalidadColaboracion>) {
    PERSONA(
        listOf(
            ModalidadColaboracion.DONACION,
            ModalidadColaboracion.VOLUNTARIADO,
            ModalidadColaboracion.COMPRA,
            ModalidadColaboracion.CAPACITACION
        )
    ),
    EMPRESA(
        listOf(
            ModalidadColaboracion.ALIANZA,
            ModalidadColaboracion.DONACION,
            ModalidadColaboracion.COMPRA,
            ModalidadColaboracion.CAPACITACION
        )
    )
}