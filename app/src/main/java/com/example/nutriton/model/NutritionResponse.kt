package com.example.nutriton.model

// Puedes adaptar esta clase según la respuesta real de la API de Edamam
// Aquí solo incluyo algunos campos comunes de ejemplo

data class NutritionResponse(
    val calories: Int?,
    val totalWeight: Double?,
    val dietLabels: List<String>?,
    val healthLabels: List<String>?,
    val cautions: List<String>?,
    val totalNutrients: Map<String, Any>?
)

