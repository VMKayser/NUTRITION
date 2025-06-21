package com.example.nutriton.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.nutriton.database.converters.DateConverters
import com.example.nutriton.database.converters.DateTimeConverters
import com.example.nutriton.database.dao.*
import com.example.nutriton.database.entities.*

@Database(
    entities = [
        Usuario::class,        TipoDietaLocal::class,
        ObjetivoLocal::class,
        CategoriaComidaLocal::class,
        PreferenciasUsuario::class,
        UsuarioAlergia::class,
        UsuarioAlimentoFavorito::class,
        HistorialPeso::class,
        CacheRecetas::class,
        PlanSemanal::class,
        ProgresoDiario::class
    ],
    version = 2, // Incrementamos la versión porque agregamos una nueva tabla
    exportSchema = false
)
@TypeConverters(DateConverters::class, DateTimeConverters::class)
abstract class NutritionDatabase : RoomDatabase() {
    
    // DAOs abstractos
    abstract fun usuarioDao(): UsuarioDao
    abstract fun tipoDietaLocalDao(): TipoDietaLocalDao
    abstract fun objetivoLocalDao(): ObjetivoLocalDao    abstract fun categoriaComidaLocalDao(): CategoriaComidaLocalDao
    abstract fun preferenciasDao(): PreferenciasDao
    abstract fun usuarioAlergiaDao(): UsuarioAlergiaDao
    abstract fun usuarioAlimentoFavoritoDao(): UsuarioAlimentoFavoritoDao
    abstract fun historialPesoDao(): HistorialPesoDao
    abstract fun cacheRecetasDao(): CacheRecetasDao
    abstract fun planSemanalDao(): PlanSemanalDao
    abstract fun progresoDiarioDao(): ProgresoDiarioDao
    
    companion object {
        @Volatile
        private var INSTANCE: NutritionDatabase? = null
        
        fun getDatabase(context: Context): NutritionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NutritionDatabase::class.java,
                    "nutrition_local_database"
                )
                .fallbackToDestructiveMigration() // Solo para desarrollo
                .build()
                INSTANCE = instance
                instance
            }
        }
        
        // Función para limpiar la instancia (útil para testing)
        fun closeDatabase() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}
