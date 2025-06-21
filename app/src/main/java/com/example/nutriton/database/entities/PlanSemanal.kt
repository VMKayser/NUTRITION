package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "plan_semanal",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoriaComidaLocal::class,
            parentColumns = ["id"],
            childColumns = ["tipo_comida_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CacheRecetas::class,
            parentColumns = ["receta_id"],
            childColumns = ["receta_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class PlanSemanal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuario_id: Int = 1, // FK → usuario
    val fecha: LocalDate,
    val tipo_comida_id: Int?, // FK → categorias_comida_local (1=Desayuno, 2=Almuerzo, etc.)
    val receta_id: Int?, // FK → cache_recetas (nullable si es comida personalizada)
    val comida_personalizada: String?, // Para cuando no es una receta del sistema
    val completada: Boolean = false,
    val fecha_creacion: LocalDateTime
)
