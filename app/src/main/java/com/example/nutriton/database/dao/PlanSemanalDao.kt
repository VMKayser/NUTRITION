package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.PlanSemanal
import kotlinx.datetime.LocalDate

// Data class para joins del plan semanal
data class PlanSemanalCompleto(
    val id: Int,
    val usuario_id: Int,
    val fecha: LocalDate,
    val tipo_comida_id: Int?,
    val receta_id: Int?,
    val comida_personalizada: String?,
    val completada: Boolean,
    val receta_nombre: String?,
    val tipo_comida_nombre: String?
)

@Dao
interface PlanSemanalDao {
    @Query("SELECT * FROM plan_semanal WHERE usuario_id = :usuarioId AND fecha = :fecha ORDER BY tipo_comida_id")
    suspend fun obtenerPlanDelDia(fecha: LocalDate, usuarioId: Int = 1): List<PlanSemanal>
    
    @Query("""
        SELECT ps.*, cr.nombre as receta_nombre, cc.nombre as tipo_comida_nombre
        FROM plan_semanal ps
        LEFT JOIN cache_recetas cr ON ps.receta_id = cr.receta_id
        LEFT JOIN categorias_comida_local cc ON ps.tipo_comida_id = cc.id
        WHERE ps.fecha = :fecha AND ps.usuario_id = :usuarioId
        ORDER BY cc.orden_visualizacion
    """)
    suspend fun obtenerPlanDelDiaCompleto(fecha: LocalDate, usuarioId: Int = 1): List<PlanSemanalCompleto>
    
    @Query("SELECT * FROM plan_semanal WHERE usuario_id = :usuarioId AND fecha >= :fechaInicio AND fecha <= :fechaFin ORDER BY fecha, tipo_comida_id")
    suspend fun obtenerPlanSemanal(fechaInicio: LocalDate, fechaFin: LocalDate, usuarioId: Int = 1): List<PlanSemanal>
    
    @Insert
    suspend fun agregarAlPlan(plan: PlanSemanal)
    
    @Update
    suspend fun actualizarPlan(plan: PlanSemanal)
    
    @Query("UPDATE plan_semanal SET completada = :completada WHERE id = :id")
    suspend fun marcarCompletada(id: Int, completada: Boolean)
    
    @Query("DELETE FROM plan_semanal WHERE id = :id")
    suspend fun eliminarDelPlan(id: Int)
    
    @Query("DELETE FROM plan_semanal WHERE fecha < :fechaLimite")
    suspend fun limpiarPlanesAntiguos(fechaLimite: LocalDate)
}
