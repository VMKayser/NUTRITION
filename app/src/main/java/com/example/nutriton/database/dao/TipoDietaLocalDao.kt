package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.TipoDietaLocal

@Dao
interface TipoDietaLocalDao {
    @Query("SELECT * FROM tipos_dieta_local WHERE activo = 1 ORDER BY nombre")
    suspend fun obtenerTiposDieta(): List<TipoDietaLocal>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTiposDieta(tipos: List<TipoDietaLocal>)
    
    @Query("SELECT * FROM tipos_dieta_local WHERE id = :id")
    suspend fun obtenerTipoDietaPorId(id: Int): TipoDietaLocal?
    
    @Query("DELETE FROM tipos_dieta_local")
    suspend fun limpiarTiposDieta()
}
