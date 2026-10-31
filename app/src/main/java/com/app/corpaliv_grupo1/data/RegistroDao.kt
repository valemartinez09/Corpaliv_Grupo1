package com.app.corpaliv_grupo1.data


import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {

    @Insert
    suspend fun insertar(registro: RegistroEntity): Long

    // Flow: el panel se actualiza solo cuando llega o se borra un registro
    @Query("SELECT * FROM registros ORDER BY fecha DESC")
    fun observarTodos(): Flow<List<RegistroEntity>>

    @Query("DELETE FROM registros WHERE id = :id")
    suspend fun eliminar(id: Long)
}