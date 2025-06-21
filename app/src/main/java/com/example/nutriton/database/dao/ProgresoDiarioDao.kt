package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.ProgresoDiario
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ProgresoDiarioDao {
    
    /**
     * Obtiene el progreso de un día específico
     */
    @Query("SELECT * FROM progreso_diario WHERE fecha = :fecha AND usuario_id = :usuarioId")
    suspend fun obtenerProgresoDia(fecha: LocalDate, usuarioId: Int = 1): ProgresoDiario?
    
    /**
     * Obtiene el progreso de varios días
     */
    @Query("SELECT * FROM progreso_diario WHERE fecha BETWEEN :fechaInicio AND :fechaFin AND usuario_id = :usuarioId ORDER BY fecha")
    suspend fun obtenerProgresoRango(fechaInicio: LocalDate, fechaFin: LocalDate, usuarioId: Int = 1): List<ProgresoDiario>
    
    /**
     * Obtiene el progreso de hoy como Flow para observar cambios
     */
    @Query("SELECT * FROM progreso_diario WHERE fecha = :fecha AND usuario_id = :usuarioId")
    fun observarProgresoHoy(fecha: LocalDate, usuarioId: Int = 1): Flow<ProgresoDiario?>
    
    /**
     * Inserta o actualiza el progreso diario
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarOActualizarProgreso(progreso: ProgresoDiario)
    
    /**
     * Marca una comida como completada
     */
    @Query("""
        UPDATE progreso_diario 
        SET desayuno_completado = CASE WHEN :tipoComida = 'DESAYUNO' THEN :completado ELSE desayuno_completado END,
            almuerzo_completado = CASE WHEN :tipoComida = 'ALMUERZO' THEN :completado ELSE almuerzo_completado END,
            cena_completada = CASE WHEN :tipoComida = 'CENA' THEN :completado ELSE cena_completada END,
            dia_completado = CASE WHEN :tipoComida = 'DESAYUNO' AND :completado = 1 
                                  THEN (almuerzo_completado = 1 AND cena_completada = 1)
                                  WHEN :tipoComida = 'ALMUERZO' AND :completado = 1 
                                  THEN (desayuno_completado = 1 AND cena_completada = 1)
                                  WHEN :tipoComida = 'CENA' AND :completado = 1 
                                  THEN (desayuno_completado = 1 AND almuerzo_completado = 1)
                                  ELSE 0 END
        WHERE fecha = :fecha AND usuario_id = :usuarioId
    """)
    suspend fun marcarComidaCompletada(fecha: LocalDate, tipoComida: String, completado: Boolean, usuarioId: Int = 1)
    
    /**
     * Actualiza los vasos de agua consumidos
     */
    @Query("UPDATE progreso_diario SET vasos_agua_consumidos = :vasos WHERE fecha = :fecha AND usuario_id = :usuarioId")
    suspend fun actualizarVasosAgua(fecha: LocalDate, vasos: Int, usuarioId: Int = 1)
    
    /**
     * Incrementa un vaso de agua
     */
    @Query("""
        UPDATE progreso_diario 
        SET vasos_agua_consumidos = vasos_agua_consumidos + 1 
        WHERE fecha = :fecha AND usuario_id = :usuarioId AND vasos_agua_consumidos < vasos_agua_objetivo
    """)
    suspend fun incrementarVasoAgua(fecha: LocalDate, usuarioId: Int = 1)
    
    /**
     * Decrementa un vaso de agua
     */
    @Query("""
        UPDATE progreso_diario 
        SET vasos_agua_consumidos = vasos_agua_consumidos - 1 
        WHERE fecha = :fecha AND usuario_id = :usuarioId AND vasos_agua_consumidos > 0
    """)
    suspend fun decrementarVasoAgua(fecha: LocalDate, usuarioId: Int = 1)
    
    /**
     * Obtiene la racha actual de días completados
     */
    @Query("""
        SELECT COUNT(*) FROM (
            SELECT fecha FROM progreso_diario 
            WHERE usuario_id = :usuarioId AND dia_completado = 1 
            AND fecha <= :fechaActual
            ORDER BY fecha DESC
        ) WHERE fecha = :fechaActual - (
            SELECT COUNT(*) FROM progreso_diario 
            WHERE usuario_id = :usuarioId AND dia_completado = 1 
            AND fecha <= :fechaActual
            ORDER BY fecha DESC
        )
    """)
    suspend fun obtenerRachaActual(fechaActual: LocalDate, usuarioId: Int = 1): Int
    
    /**
     * Crea un registro de progreso diario si no existe
     */
    suspend fun crearProgresoSiNoExiste(fecha: LocalDate, usuarioId: Int = 1) {
        val existente = obtenerProgresoDia(fecha, usuarioId)
        if (existente == null) {
            insertarOActualizarProgreso(
                ProgresoDiario(
                    fecha = fecha,
                    usuario_id = usuarioId
                )
            )
        }
    }
}
