package com.app.corpaliv_grupo1.repository

import android.content.Context
import com.app.corpaliv_grupo1.data.AppDatabase
import com.app.corpaliv_grupo1.data.RegistroEntity
import com.app.corpaliv_grupo1.model.FrecuenciaDonacion
import com.app.corpaliv_grupo1.model.ModalidadColaboracion
import com.app.corpaliv_grupo1.model.Registro
import com.app.corpaliv_grupo1.model.TipoColaborador
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RegistroRepository private constructor(context: Context) {

    private val dao = AppDatabase.getInstance(context).registroDao()

    /** Registros del más reciente al más antiguo; se emite de nuevo cada vez que cambia la tabla. */

    val registros: Flow<List<Registro>> =
        dao.observarTodos().map { lista -> lista.mapNotNull { it.aModelo() } }

    suspend fun guardar(registro: Registro): Long = dao.insertar(registro.aEntidad())

    suspend fun eliminar(id: Long) = dao.eliminar(id)

    private fun Registro.aEntidad() = RegistroEntity(
        id = id,
        tipo = tipo.name,
        nombre = nombre,
        contacto = contacto,
        correo = correo,
        modalidad = modalidad.name,
        frecuencia = frecuencia?.name,
        mensaje = mensaje,
        fecha = fecha
    )

    // Si una fila trae un valor inválido se ignora, en vez de botar la app
    private fun RegistroEntity.aModelo(): Registro? = runCatching {
        Registro(
            id = id,
            tipo = TipoColaborador.valueOf(tipo),
            nombre = nombre,
            contacto = contacto,
            correo = correo,
            modalidad = ModalidadColaboracion.valueOf(modalidad),
            frecuencia = frecuencia?.let { FrecuenciaDonacion.valueOf(it) },
            mensaje = mensaje,
            fecha = fecha
        )
    }.getOrNull()

    companion object {
        @Volatile
        private var INSTANCE: RegistroRepository? = null

        fun getInstance(context: Context): RegistroRepository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: RegistroRepository(context.applicationContext).also { INSTANCE = it }
            }
    }
}