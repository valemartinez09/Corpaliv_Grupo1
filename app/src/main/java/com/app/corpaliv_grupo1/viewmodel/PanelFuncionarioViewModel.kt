package com.app.corpaliv_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.data.remote.dto.RegistroResponse
import com.app.corpaliv_grupo1.repository.RegistroRemoteRepository
import com.app.corpaliv_grupo1.repository.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PanelUiState(
    val registros: List<RegistroResponse> = emptyList(),
    val cargando: Boolean = true,
    val errorCarga: Boolean = false,
    val procesandoId: Long? = null,   // registro con una acción en curso
    val errorAccion: Boolean = false
)

class PanelFuncionarioViewModel @JvmOverloads constructor(
    private val repositorio: RegistroRemoteRepository = RegistroRemoteRepository()
) : ViewModel() {

    private val _estado = MutableStateFlow(PanelUiState())
    val estado: StateFlow<PanelUiState> = _estado.asStateFlow()

    init { cargar() }

    fun cargar() {
        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, errorCarga = false) }
            when (val r = repositorio.listar()) {
                is Resultado.Exito -> _estado.update {
                    it.copy(
                        // Más recientes primero
                        registros = r.datos.sortedByDescending { reg -> reg.fecha },
                        cargando = false
                    )
                }
                is Resultado.Error -> _estado.update {
                    it.copy(cargando = false, errorCarga = true)
                }
            }
        }
    }

    fun marcarContactado(id: Long) {
        viewModelScope.launch {
            _estado.update { it.copy(procesandoId = id, errorAccion = false) }
            when (val r = repositorio.marcarContactado(id)) {
                is Resultado.Exito -> _estado.update { s ->
                    s.copy(
                        procesandoId = null,
                        registros = s.registros.map { if (it.id == id) r.datos else it }
                    )
                }
                is Resultado.Error -> _estado.update {
                    it.copy(procesandoId = null, errorAccion = true)
                }
            }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            _estado.update { it.copy(procesandoId = id, errorAccion = false) }
            when (repositorio.eliminar(id)) {
                is Resultado.Exito -> _estado.update { s ->
                    s.copy(
                        procesandoId = null,
                        registros = s.registros.filterNot { it.id == id }
                    )
                }
                is Resultado.Error -> _estado.update {
                    it.copy(procesandoId = null, errorAccion = true)
                }
            }
        }
    }

    fun descartarErrorAccion() = _estado.update { it.copy(errorAccion = false) }
}