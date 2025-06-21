package com.example.nutriton.database.dao

import androidx.room.*
import com.example.nutriton.database.entities.Usuario

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario WHERE id = 1")
    suspend fun obtenerUsuario(): Usuario?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: Usuario)
    
    @Update
    suspend fun actualizarUsuario(usuario: Usuario)
}
