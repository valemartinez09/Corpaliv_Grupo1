package com.app.corpaliv_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.data.remote.dto.ProgramaResponse
import com.app.corpaliv_grupo1.repository.ProgramaRepository
import com.app.corpaliv_grupo1.repository.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProgramasUiState(
    val cargando: Boolean = true,
    val programas: List<ProgramaResponse> = emptyList(),
    val error: String? = null
)

class ProgramasViewModel(
    private val repositorio: ProgramaRepository = ProgramaRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow(ProgramasUiState())
    val estado: StateFlow<ProgramasUiState> = _estado.asStateFlow()

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, error = null) }
            when (val resultado = repositorio.listar()) {
                is Resultado.Exito -> _estado.update {
                    it.copy(cargando = false, programas = resultado.datos.sortedBy { p -> p.orden })
                }
                is Resultado.Error -> _estado.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}