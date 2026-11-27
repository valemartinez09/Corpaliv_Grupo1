package com.app.corpaliv_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.model.IndicadoresUiState
import com.app.corpaliv_grupo1.repository.IndicadoresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IndicadoresViewModel(
    private val repositorio: IndicadoresRepository = IndicadoresRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow<IndicadoresUiState>(IndicadoresUiState.Cargando)
    val estado: StateFlow<IndicadoresUiState> = _estado.asStateFlow()

    init {
        cargar()
    }

    /** Consulta la API externa. También se usa para el botón "Reintentar". */
    fun cargar() {
        viewModelScope.launch {
            _estado.value = IndicadoresUiState.Cargando
            repositorio.obtenerIndicadores()
                .onSuccess { _estado.value = IndicadoresUiState.Exito(it) }
                .onFailure { _estado.value = IndicadoresUiState.Fallo }
        }
    }
}