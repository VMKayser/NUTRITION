package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.CategoriaComidaLocal

@Dao
interface CategoriaComidaLocalDao {
    @Query("SELECT * FROM categorias_comida_local WHERE activo = 1 ORDER BY orden_visualizacion")
    suspend fun obtenerCategorias(): List<CategoriaComidaLocal>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCategorias(categorias: List<CategoriaComidaLocal>)
    
    @Query("SELECT * FROM categorias_comida_local WHERE id = :id")
    suspend fun obtenerCategoriaPorId(id: Int): CategoriaComidaLocal?
    
    @Query("DELETE FROM categorias_comida_local")
    suspend fun limpiarCategorias()
}
