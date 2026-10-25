package com.app.corpaliv_grupo1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app.corpaliv_grupo1.data.EstadoDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// AndroidViewModel porque necesitamos el contexto de la aplicación para DataStore
class EstadoViewModel(application: Application) : AndroidViewModel(application) {

    private val estadoDataStore = EstadoDataStore(application)

    // null = todavía cargando (se muestra el loader)
    private val _activo = MutableStateFlow<Boolean?>(null)
    val activo: StateFlow<Boolean?> = _activo

    // Controla el mensaje animado de éxito
    private val _mostrarMensaje = MutableStateFlow(false)
    val mostrarMensaje: StateFlow<Boolean> = _mostrarMensaje

    init {
        cargarEstado()
    }

    private fun cargarEstado() {
        viewModelScope.launch {
            delay(1500) // simula una carga para ver el loader
            _activo.value = estadoDataStore.obtenerEstado().first() ?: false
        }
    }

    fun alternarEstado() {
        viewModelScope.launch {
            val nuevoValor = !(_activo.value ?: false)
            estadoDataStore.guardarEstado(nuevoValor) // persiste
            _activo.value = nuevoValor
            _mostrarMensaje.value = true
            delay(2000)
            _mostrarMensaje.value = false
        }
    }
}