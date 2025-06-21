package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.PreferenciasUsuario

// Data class para joins con nombres descriptivos
data class PreferenciasCompletas(
    val id: Int,
    val tipo_dieta_id: Int?,
    val objetivo_id: Int?,
    val porcentaje_proteinas: Int,
    val porcentaje_carbohidratos: Int,
    val porcentaje_grasas: Int,
    val nivel_actividad: String,
    val peso_objetivo: Float?,
    val tipo_dieta_nombre: String?,
    val objetivo_nombre: String?
)

@Dao
interface PreferenciasDao {
    @Query("SELECT * FROM preferencias_usuario WHERE id = 1")
    suspend fun obtenerPreferencias(): PreferenciasUsuario?
    
    @Query("""
        SELECT p.*, td.nombre as tipo_dieta_nombre, o.nombre as objetivo_nombre 
        FROM preferencias_usuario p
        LEFT JOIN tipos_dieta_local td ON p.tipo_dieta_id = td.id
        LEFT JOIN objetivos_local o ON p.objetivo_id = o.id
        WHERE p.id = 1
    """)
    suspend fun obtenerPreferenciasCompletas(): PreferenciasCompletas?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPreferencias(preferencias: PreferenciasUsuario)
    
    @Update
    suspend fun actualizarPreferencias(preferencias: PreferenciasUsuario)
}
