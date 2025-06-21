package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.UsuarioAlimentoFavorito

@Dao
interface UsuarioAlimentoFavoritoDao {
    @Query("SELECT * FROM usuario_alimentos_favoritos WHERE usuario_id = :usuarioId ORDER BY alimento")
    suspend fun obtenerAlimentosFavoritos(usuarioId: Int = 1): List<UsuarioAlimentoFavorito>
    
    @Insert
    suspend fun agregarAlimentoFavorito(alimento: UsuarioAlimentoFavorito)
    
    @Query("DELETE FROM usuario_alimentos_favoritos WHERE id = :id")
    suspend fun eliminarAlimentoFavorito(id: Int)
    
    @Query("SELECT COUNT(*) > 0 FROM usuario_alimentos_favoritos WHERE usuario_id = :usuarioId AND alimento = :alimento")
    suspend fun tieneAlimentoFavorito(usuarioId: Int = 1, alimento: String): Boolean
    
    @Query("DELETE FROM usuario_alimentos_favoritos WHERE usuario_id = :usuarioId")
    suspend fun limpiarAlimentosFavoritos(usuarioId: Int = 1)
}
