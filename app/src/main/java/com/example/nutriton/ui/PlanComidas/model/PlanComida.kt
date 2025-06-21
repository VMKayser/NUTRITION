package com.example.nutriton.ui.PlanComidas.model

data class TipoDieta(
    val id: String, // Cambiado a String para coincidir con la API
    val nombre: String,
    val descripcion: String
)

data class PlanComida(
    val id: String, // Cambiado a String para coincidir con la API
    val nombre: String,
    val descripcion: String,
    val imagenUrl: String?,
    val categoria: String,
    val duracion: String,
    val calorias: Int,
    val tipoDieta: TipoDieta? = null,
    val totalRecetas: Int = 0
)
