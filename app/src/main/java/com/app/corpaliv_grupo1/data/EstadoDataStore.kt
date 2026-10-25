package com.app.corpaliv_grupo1.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore con nombre "preferencias_usuario"
val Context.dataStore by preferencesDataStore(name = "preferencias_usuario")

class EstadoDataStore(private val context: Context) {

    // Clave con la que se guarda el estado
    private val ESTADO_ACTIVADO = booleanPreferencesKey("modo_activado")

    // Guarda el estado en el almacenamiento local
    suspend fun guardarEstado(valor: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[ESTADO_ACTIVADO] = valor
        }
    }

    // Lee el estado como Flow (null si nunca se guardó)
    fun obtenerEstado(): Flow<Boolean?> {
        return context.dataStore.data.map { preferencias ->
            preferencias[ESTADO_ACTIVADO]
        }
    }
}
