package com.app.corpaliv_grupo1.viewmodel

import androidx.lifecycle.ViewModel
import com.app.corpaliv_grupo1.model.UsuarioErrores
import com.app.corpaliv_grupo1.model.UsuarioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class UsuarioViewModel : ViewModel() {

    // Estado interno (mutable) y expuesto (solo lectura) para la UI
    private val _estado = MutableStateFlow(UsuarioUiState())
    val estado: StateFlow<UsuarioUiState> = _estado

    // Cada función actualiza su campo y limpia el error de ese campo
    fun onNombreChange(valor: String) {
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }
    }

    fun onCorreoChange(valor: String) {
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }
    }

    fun onClaveChange(valor: String) {
        _estado.update { it.copy(clave = valor, errores = it.errores.copy(clave = null)) }
    }

    fun onDireccionChange(valor: String) {
        _estado.update { it.copy(direccion = valor, errores = it.errores.copy(direccion = null)) }
    }

    fun onAceptarTerminosChange(valor: Boolean) {
        _estado.update { it.copy(aceptaTerminos = valor) }
    }

    // Valida todo el formulario; devuelve true si no hay errores
    fun validarFormulario(): Boolean {
        val actual = _estado.value
        val errores = UsuarioErrores(
            nombre = if (actual.nombre.isBlank()) "Campo obligatorio" else null,
            correo = if (!actual.correo.contains("@")) "Correo inválido" else null,
            clave = if (actual.clave.length < 6) "Debe tener al menos 6 caracteres" else null,
            direccion = if (actual.direccion.isBlank()) "Campo obligatorio" else null
        )

        val hayErrores = listOfNotNull(
            errores.nombre, errores.correo, errores.clave, errores.direccion
        ).isNotEmpty()

        _estado.update { it.copy(errores = errores) }
        return !hayErrores
    }
}