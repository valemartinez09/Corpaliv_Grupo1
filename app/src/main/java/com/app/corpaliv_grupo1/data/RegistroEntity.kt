package com.app.corpaliv_grupo1.data

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "registros")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val tipo: String,
    val nombre: String,
    val contacto: String,
    val correo: String,
    val modalidad: String,
    val frecuencia: String?,
    val mensaje: String,
    val fecha: Long
)