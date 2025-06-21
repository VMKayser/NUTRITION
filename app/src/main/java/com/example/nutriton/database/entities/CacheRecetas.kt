package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "cache_recetas",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaComidaLocal::class,
            parentColumns = ["id"],
            childColumns = ["categoria_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class CacheRecetas(
    @PrimaryKey val receta_id: Int, // ID de la receta remota
    val nombre: String,
    val descripcion: String,
    val ingredientes: String,
    val instrucciones: String,
    val tiempo_preparacion: Int,
    val nivel_dificultad: String,
    val imagen_url: String?,
    val categoria_id: Int?, // FK → categorias_comida_local
    val calorias: Float,
    val proteinas: Float,
    val carbohidratos: Float,
    val grasas: Float,
    val fecha_descarga: LocalDateTime,
    val fecha_ultimo_acceso: LocalDateTime
)
