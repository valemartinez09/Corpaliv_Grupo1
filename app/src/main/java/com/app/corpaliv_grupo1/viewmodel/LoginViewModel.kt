package com.app.corpaliv_grupo1.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    @StringRes val errorUsuario: Int? = null,
    @StringRes val errorClave: Int? = null,
    @StringRes val errorGeneral: Int? = null,
    val cargando: Boolean = false,
    val autenticado: Boolean = false
)

class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    fun onUsuarioChange(valor: String) =
        _estado.update { it.copy(usuario = valor, errorUsuario = null, errorGeneral = null) }

    fun onClaveChange(valor: String) =
        _estado.update { it.copy(clave = valor, errorClave = null, errorGeneral = null) }

    fun ingresar() {
        val s = _estado.value
        if (s.cargando) return

        val errorUsuario = if (s.usuario.isBlank()) R.string.error_usuario else null
        val errorClave = if (s.clave.isBlank()) R.string.error_clave else null
        if (errorUsuario != null || errorClave != null) {
            _estado.update {
                it.copy(errorUsuario = errorUsuario, errorClave = errorClave, errorGeneral = null)
            }
            return
        }

        viewModelScope.launch {
            _estado.update { it.copy(cargando = true, errorGeneral = null) }
            delay(800) // simula la consulta al backend
            val correcto = s.usuario.trim() == USUARIO_DEMO && s.clave == CLAVE_DEMO
            _estado.update {
                if (correcto) it.copy(cargando = false, autenticado = true)
                else it.copy(cargando = false, errorGeneral = R.string.error_credenciales)
            }
        }
    }

    private companion object {
        // Credenciales ficticias solo para el MVP académico.
        const val USUARIO_DEMO = "funcionario"
        const val CLAVE_DEMO = "alba2026"
    }
}