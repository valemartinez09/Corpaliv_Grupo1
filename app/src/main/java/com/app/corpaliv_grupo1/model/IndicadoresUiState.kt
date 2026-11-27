package com.app.corpaliv_grupo1.model

sealed interface IndicadoresUiState {
    data object Cargando : IndicadoresUiState
    data class Exito(val indicadores: List<Indicador>) : IndicadoresUiState
    data object Fallo : IndicadoresUiState
}