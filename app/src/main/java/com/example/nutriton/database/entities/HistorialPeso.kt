package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "historial_peso",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class HistorialPeso(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuario_id: Int = 1, // FK → usuario
    val peso: Float,
    val fecha: LocalDate,
    val notas: String = ""
)
