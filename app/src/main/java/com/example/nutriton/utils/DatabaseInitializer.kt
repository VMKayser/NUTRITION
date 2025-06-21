package com.example.nutriton.utils

import android.content.Context
import androidx.lifecycle.lifecycleScope
import com.example.nutriton.repository.NutritionRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.LifecycleOwner

/**
 * Utilidad para inicializar la base de datos en MainActivity
 */
object DatabaseInitializer {
    
    suspend fun initializeDatabase(context: Context) {
        val repository = NutritionRepository(context)
        
        try {
            // Inicializar datos maestros (categorías, tipos de dieta, objetivos)
            repository.inicializarDatosMaestros()
            
            // Verificar si existe usuario, si no crear uno por defecto
            val usuarioExistente = repository.obtenerUsuario()
            if (usuarioExistente == null) {
                // El usuario se creará automáticamente en el ViewModel cuando sea necesario
                android.util.Log.d("DatabaseInit", "Base de datos inicializada correctamente")
            }
            
        } catch (e: Exception) {
            android.util.Log.e("DatabaseInit", "Error inicializando base de datos", e)
        }
    }
}
