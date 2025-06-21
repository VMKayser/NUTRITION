package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDateTime

@Entity(
    tableName = "preferencias_usuario",
    foreignKeys = [
        ForeignKey(
            entity = TipoDietaLocal::class, 
            parentColumns = ["id"], 
            childColumns = ["tipo_dieta_id"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ObjetivoLocal::class, 
            parentColumns = ["id"], 
            childColumns = ["objetivo_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class PreferenciasUsuario(
    @PrimaryKey val id: Int = 1,
    val tipo_dieta_id: Int?,
    val objetivo_id: Int?,
    val porcentaje_proteinas: Int = 25,
    val porcentaje_carbohidratos: Int = 50,
    val porcentaje_grasas: Int = 25,
    val nivel_actividad: String = "Moderado", // Sedentario, Moderado, Activo, Muy Activo
    val peso_objetivo: Float?,
    val fecha_actualizacion: LocalDateTime
)
