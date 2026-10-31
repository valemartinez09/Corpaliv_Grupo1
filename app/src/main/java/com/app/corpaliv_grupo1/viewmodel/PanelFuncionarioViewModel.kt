package com.app.corpaliv_grupo1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.model.Registro
import com.app.corpaliv_grupo1.repository.RegistroRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PanelUiState(
    val registros: List<Registro> = emptyList(),
    val cargando: Boolean = true
)

class PanelFuncionarioViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = RegistroRepository.getInstance(application)

    val estado: StateFlow<PanelUiState> = repositorio.registros
        .map { PanelUiState(registros = it, cargando = false) }
        .catch { emit(PanelUiState(cargando = false)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PanelUiState())

    fun eliminar(id: Long) {
        viewModelScope.launch {
            runCatching { repositorio.eliminar(id) }
        }
    }
}