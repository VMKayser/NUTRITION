package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.UsuarioAlergia

@Dao
interface UsuarioAlergiaDao {
    @Query("SELECT * FROM usuario_alergias WHERE usuario_id = :usuarioId ORDER BY alergia")
    suspend fun obtenerAlergias(usuarioId: Int = 1): List<UsuarioAlergia>
    
    @Insert
    suspend fun agregarAlergia(alergia: UsuarioAlergia)
    
    @Query("DELETE FROM usuario_alergias WHERE id = :id")
    suspend fun eliminarAlergia(id: Int)
    
    @Query("SELECT COUNT(*) > 0 FROM usuario_alergias WHERE usuario_id = :usuarioId AND alergia = :alergia")
    suspend fun tieneAlergia(usuarioId: Int = 1, alergia: String): Boolean
    
    @Query("DELETE FROM usuario_alergias WHERE usuario_id = :usuarioId")
    suspend fun limpiarAlergias(usuarioId: Int = 1)
}
