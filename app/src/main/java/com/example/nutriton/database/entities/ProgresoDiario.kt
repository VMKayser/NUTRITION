package com.example.nutriton.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

/**
 * Entidad para rastrear el progreso diario del usuario
 */
@Entity(tableName = "progreso_diario")
data class ProgresoDiario(
    @PrimaryKey val fecha: LocalDate,
    val usuario_id: Int = 1,
    val desayuno_completado: Boolean = false,
    val almuerzo_completado: Boolean = false,
    val cena_completada: Boolean = false,
    val vasos_agua_consumidos: Int = 0,
    val vasos_agua_objetivo: Int = 6,
    val dia_completado: Boolean = false // Se marca true cuando se completan todas las comidas
) {
    /**
     * Verifica si todas las comidas del día están completadas
     */
    fun todasLasComidasCompletadas(): Boolean {
        return desayuno_completado && almuerzo_completado && cena_completada
    }
    
    /**
     * Calcula el porcentaje de progreso del día
     */
    fun porcentajeProgreso(): Int {
        var completadas = 0
        if (desayuno_completado) completadas++
        if (almuerzo_completado) completadas++
        if (cena_completada) completadas++
        return (completadas * 100) / 3
    }
    
    /**
     * Calcula el porcentaje de hidratación
     */
    fun porcentajeHidratacion(): Int {
        return if (vasos_agua_objetivo > 0) {
            ((vasos_agua_consumidos * 100) / vasos_agua_objetivo).coerceAtMost(100)
        } else 0
    }
}
