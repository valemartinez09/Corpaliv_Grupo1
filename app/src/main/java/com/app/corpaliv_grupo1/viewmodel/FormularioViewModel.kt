package com.app.corpaliv_grupo1.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.R
import com.app.corpaliv_grupo1.model.EstadoEnvio
import com.app.corpaliv_grupo1.model.FormularioErrores
import com.app.corpaliv_grupo1.model.FormularioUiState
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.TipoColaborador
import com.app.corpaliv_grupo1.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormularioViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    // El tipo (persona/empresa) llega como argumento de la ruta.
    private val tipo: TipoColaborador =
        savedStateHandle.get<String>(Routes.Formulario.ARG_TIPO)
            ?.let { runCatching { TipoColaborador.valueOf(it) }.getOrNull() }
            ?: TipoColaborador.PERSONA

    private val _estado = MutableStateFlow(FormularioUiState(tipo = tipo))
    val estado: StateFlow<FormularioUiState> = _estado.asStateFlow()

    fun onNombreChange(valor: String) =
        _estado.update { it.copy(nombre = valor, errores = it.errores.copy(nombre = null)) }

    fun onContactoChange(valor: String) =
        _estado.update { it.copy(contacto = valor, errores = it.errores.copy(contacto = null)) }

    fun onCorreoChange(valor: String) =
        _estado.update { it.copy(correo = valor, errores = it.errores.copy(correo = null)) }

    fun onModalidadChange(valor: ModalidadColaboracion) =
        _estado.update { it.copy(modalidad = valor, errores = it.errores.copy(modalidad = null)) }

    fun onMensajeChange(valor: String) =
        _estado.update { it.copy(mensaje = valor) }

    fun onTerminosChange(valor: Boolean) =
        _estado.update { it.copy(aceptaTerminos = valor, errores = it.errores.copy(terminos = null)) }

    fun validar(): Boolean {
        val s = _estado.value
        val errores = FormularioErrores(
            nombre = if (s.nombre.isBlank()) R.string.error_nombre else null,
            contacto = if (s.tipo == TipoColaborador.EMPRESA && s.contacto.isBlank())
                R.string.error_contacto else null,
            correo = if (!CORREO_REGEX.matches(s.correo.trim())) R.string.error_correo else null,
            modalidad = if (s.modalidad == null) R.string.error_modalidad else null,
            terminos = if (!s.aceptaTerminos) R.string.error_terminos else null
        )
        _estado.update { it.copy(errores = errores) }
        return errores.sinErrores()
    }

    fun enviar() {
        if (_estado.value.envio != EstadoEnvio.EDITANDO) return
        if (!validar()) return

        viewModelScope.launch {
            _estado.update { it.copy(envio = EstadoEnvio.ENVIANDO) }
            // TODO: aquí se guardará el registro en la base local (Room) y luego en el backend.
            delay(2500)
            _estado.update { it.copy(envio = EstadoEnvio.ENVIADO) }
        }
    }

    private companion object {
        val CORREO_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}