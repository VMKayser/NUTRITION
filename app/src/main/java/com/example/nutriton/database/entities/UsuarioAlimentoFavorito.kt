package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(
    tableName = "usuario_alimentos_favoritos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuario_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UsuarioAlimentoFavorito(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuario_id: Int = 1, // FK → usuario
    val alimento: String, // "Pollo", "Brócoli", "Quinoa", etc.
    val fecha_agregada: LocalDate
)
