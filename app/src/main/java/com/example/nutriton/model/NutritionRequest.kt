package com.example.nutriton.model

data class NutritionRequest(
    val ingr: List<String>
    // Puedes agregar más campos si la API lo requiere, por ahora solo ingr
    // val title: String? = null
)
