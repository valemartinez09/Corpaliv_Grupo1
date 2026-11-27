package com.app.corpaliv_grupo1.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.repository.FuncionarioRepository
import com.app.corpaliv_grupo1.repository.Resultado
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
    val errorGeneral: String? = null,
    val cargando: Boolean = false,
    val autenticado: Boolean = false
)

class LoginViewModel(
    private val repositorio: FuncionarioRepository = FuncionarioRepository()
) : ViewModel() {

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

            when (val resultado = repositorio.login(s.usuario, s.clave)) {
                is Resultado.Exito -> _estado.update {
                    it.copy(cargando = false, autenticado = true)
                }
                is Resultado.Error -> _estado.update {
                    it.copy(cargando = false, errorGeneral = resultado.mensaje)
                }
            }
        }
    }
}