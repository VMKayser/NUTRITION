package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.HistorialPeso
import kotlinx.datetime.LocalDate

@Dao
interface HistorialPesoDao {
    @Query("SELECT * FROM historial_peso WHERE usuario_id = :usuarioId ORDER BY fecha DESC")
    suspend fun obtenerHistorial(usuarioId: Int = 1): List<HistorialPeso>
    
    @Insert
    suspend fun agregarPeso(peso: HistorialPeso)
    
    @Query("SELECT * FROM historial_peso WHERE usuario_id = :usuarioId ORDER BY fecha DESC LIMIT 1")
    suspend fun obtenerUltimoPeso(usuarioId: Int = 1): HistorialPeso?
    
    @Query("SELECT * FROM historial_peso WHERE usuario_id = :usuarioId AND fecha >= :fechaInicio AND fecha <= :fechaFin ORDER BY fecha DESC")
    suspend fun obtenerHistorialPorRango(usuarioId: Int = 1, fechaInicio: LocalDate, fechaFin: LocalDate): List<HistorialPeso>
    
    @Query("DELETE FROM historial_peso WHERE id = :id")
    suspend fun eliminarRegistroPeso(id: Int)
    
    @Update
    suspend fun actualizarRegistroPeso(peso: HistorialPeso)
}
