package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(tableName = "categorias_comida_local")
data class CategoriaComidaLocal(
    @PrimaryKey val id: Int, // mismo ID que en la BD remota
    val nombre: String,
    val descripcion: String,
    val orden_visualizacion: Int = 0,
    val activo: Boolean = true,
    val fecha_actualizacion: LocalDateTime
)
