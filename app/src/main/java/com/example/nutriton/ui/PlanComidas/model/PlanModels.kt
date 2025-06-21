package com.example.nutriton.ui.PlanComidas.model

data class ComidaCompleta(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val imagenUrl: String?,
    val categoria: String,
    val calorias: Int,
    val tiempoCoccion: Int? = null,
    val porciones: Int = 1,
    val ingredientes: String? = null,
    val instrucciones: String? = null,
    val recetaNombre: String? = null,
    val comidaDia: ComidaDia? = null,
    val informacionNutricional: InfoNutricional? = null
) {
    val imagen: String? get() = imagenUrl
}

data class InfoNutricional(
    val calorias: Int,
    val proteinas: Float = 0f,
    val carbohidratos: Float = 0f,
    val grasas: Float = 0f
)

data class ComidaDia(
    val id: Int,
    val categoriaComidaId: Int
)

data class DiaPlan(
    val id: Int,
    val numeroDia: Int,
    val planId: Int,
    val nombreDia: String
)
