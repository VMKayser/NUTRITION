package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.ObjetivoLocal

@Dao
interface ObjetivoLocalDao {
    @Query("SELECT * FROM objetivos_local WHERE activo = 1 ORDER BY nombre")
    suspend fun obtenerObjetivos(): List<ObjetivoLocal>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarObjetivos(objetivos: List<ObjetivoLocal>)
    
    @Query("SELECT * FROM objetivos_local WHERE id = :id")
    suspend fun obtenerObjetivoPorId(id: Int): ObjetivoLocal?
    
    @Query("DELETE FROM objetivos_local")
    suspend fun limpiarObjetivos()
}
