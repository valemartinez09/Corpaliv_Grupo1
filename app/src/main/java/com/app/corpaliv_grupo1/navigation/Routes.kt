package com.app.corpaliv_grupo1.navigation

import com.app.corpaliv_grupo1.model.TipoColaborador

sealed class Routes(val route: String) {
    object Inicio : Routes("inicio")
    object QuienesSomos : Routes("quienes_somos")
    object Colaborar : Routes("colaborar")
    object Formulario : Routes("formulario/{tipo}") {
        const val ARG_TIPO = "tipo"
        fun crear(tipo: TipoColaborador) = "formulario/${tipo.name}"
    }
    object LoginFuncionario : Routes("funcionario/login")
    object PanelFuncionario : Routes("funcionario/panel")
}