package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "objetivos_local")
data class ObjetivoLocal(
    @PrimaryKey val id: Int, // mismo ID que en la BD remota
    val nombre: String,
    val descripcion: String,
    val activo: Boolean = true,
    val fecha_actualizacion: LocalDateTime
)
