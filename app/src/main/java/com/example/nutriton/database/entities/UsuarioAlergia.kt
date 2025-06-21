package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "usuario_alergias",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsuarioAlergia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuario_id: Int = 1, // FK → usuario
    val alergia: String, // "Gluten", "Lactosa", "Nueces", etc.
    val fecha_agregada: LocalDate
)
