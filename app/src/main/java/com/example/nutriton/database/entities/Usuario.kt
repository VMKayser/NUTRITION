package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey val id: Int = 1, // Solo un usuario por app
    val nombre: String,
    val edad: Int?,
    val altura: Float?, // en cm
    val peso_actual: Float?, // en kg
    val genero: String?, // M, F, Otro
    val fecha_registro: LocalDate
)
