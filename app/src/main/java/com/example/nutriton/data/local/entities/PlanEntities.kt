package com.example.nutriton.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planes_comida_local")
data class PlanComidaLocal(
    @PrimaryKey val id: Int,
    val tipoDietaId: Int,
    val nombre: String,
    val descripcion: String,
    val duracionDias: Int,
    val fechaCreacion: Long = System.currentTimeMillis()
)

@Entity(tableName = "dias_plan")
data class DiaPlan(
    @PrimaryKey val id: Int,
    val planId: Int,
    val numeroDia: Int,
    val fecha: String? = null
)

@Entity(tableName = "comidas_dia")
data class ComidaDia(
    @PrimaryKey val id: Int,
    val diaPlanId: Int,
    val categoriaComidaId: Int, // 1=Desayuno, 2=Almuerzo, 3=Cena
    val recetaId: Int,
    val orden: Int = 0
)

data class ComidaCompleta(
    val comidaDia: ComidaDia,
    val recetaNombre: String,
    val recetaDescripcion: String,
    val recetaImagenUrl: String?,
    val calorias: Int,
    val porciones: Int,
    val tiempoPreparacion: Int,
    val categoriaNombre: String
)
