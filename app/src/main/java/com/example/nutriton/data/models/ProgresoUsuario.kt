package com.example.nutriton.data.models

/**
 * Modelo de datos para el progreso diario del usuario
 */
data class ProgresoUsuario(
    val diaActual: Int = 1,
    val totalDias: Int = 5,
    val diasCompletados: List<Boolean> = List(5) { false },
    val desayunoCompletado: Boolean = false,
    val almuerzoCompletado: Boolean = false,
    val cenaCompletada: Boolean = false,
    val vasosAguaActuales: Int = 0,
    val vasosAguaObjetivo: Int = 6
) {
    /**
     * Calcula el porcentaje de días completados
     */
    fun porcentajeDiasCompletados(): Int {
        val completados = diasCompletados.count { it }
        return if (totalDias > 0) (completados * 100 / totalDias) else 0
    }
    
    /**
     * Verifica si el día actual está completado
     */
    fun esDiaActualCompletado(): Boolean {
        return desayunoCompletado && almuerzoCompletado && cenaCompletada
    }
    
    /**
     * Calcula el porcentaje de comidas completadas hoy
     */
    fun porcentajeComidasHoy(): Int {
        val comidas = listOf(desayunoCompletado, almuerzoCompletado, cenaCompletada)
        val completadas = comidas.count { it }
        return (completadas * 100 / 3)
    }
    
    /**
     * Calcula el porcentaje de agua consumida
     */
    fun porcentajeAgua(): Int {
        return if (vasosAguaObjetivo > 0) (vasosAguaActuales * 100 / vasosAguaObjetivo) else 0
    }
}
