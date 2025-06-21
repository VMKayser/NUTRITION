package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.CacheRecetas
import kotlinx.datetime.LocalDateTime

@Dao
interface CacheRecetasDao {
    @Query("SELECT * FROM cache_recetas ORDER BY fecha_ultimo_acceso DESC")
    suspend fun obtenerRecetasCache(): List<CacheRecetas>
    
    @Query("SELECT * FROM cache_recetas WHERE categoria_id = :categoriaId ORDER BY fecha_ultimo_acceso DESC")
    suspend fun obtenerRecetasPorCategoria(categoriaId: Int): List<CacheRecetas>
    
    @Query("SELECT * FROM cache_recetas WHERE receta_id = :recetaId")
    suspend fun obtenerRecetaPorId(recetaId: Int): CacheRecetas?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarRecetaEnCache(receta: CacheRecetas)
    
    @Query("UPDATE cache_recetas SET fecha_ultimo_acceso = :fecha WHERE receta_id = :recetaId")
    suspend fun actualizarUltimoAcceso(recetaId: Int, fecha: LocalDateTime)
    
    @Query("DELETE FROM cache_recetas WHERE fecha_ultimo_acceso < :fechaLimite")
    suspend fun limpiarCacheAntiguo(fechaLimite: LocalDateTime)
    
    @Query("DELETE FROM cache_recetas")
    suspend fun limpiarTodoElCache()
    
    @Query("SELECT COUNT(*) FROM cache_recetas")
    suspend fun contarRecetasEnCache(): Int
}
