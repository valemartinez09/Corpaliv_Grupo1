package com.app.corpaliv_grupo1.navigation

import com.app.corpaliv_grupo1.model.FrecuenciaDonacion
import com.app.corpaliv_grupo1.model.TipoColaborador

sealed class Routes(val route: String) {
    object Inicio : Routes("inicio")
    object QuienesSomos : Routes("quienes_somos")
    object Colaborar : Routes("colaborar")
    object AportePersona : Routes("aporte_persona")
    object Formulario : Routes("formulario/{tipo}?frecuencia={frecuencia}") {
        const val ARG_TIPO = "tipo"
        const val ARG_FRECUENCIA = "frecuencia"
        fun crear(tipo: TipoColaborador, frecuencia: FrecuenciaDonacion? = null) =
            "formulario/${tipo.name}" + (frecuencia?.let { "?frecuencia=${it.name}" } ?: "")
    }
    object LoginFuncionario : Routes("funcionario/login")
    object PanelFuncionario : Routes("funcionario/panel")
}