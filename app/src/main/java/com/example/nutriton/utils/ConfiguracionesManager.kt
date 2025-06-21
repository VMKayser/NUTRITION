package com.example.nutriton.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Administrador de configuraciones usando SharedPreferences
 */
class ConfiguracionesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        "configuraciones_nutriton", 
        Context.MODE_PRIVATE
    )
    
    companion object {
        private const val KEY_RECORDATORIO_AGUA = "recordatorio_agua"
        private const val KEY_SEGUIMIENTO_DIETA = "seguimiento_dieta"
        private const val KEY_PESAJE_AUTOMATICO = "pesaje_automatico"
        private const val KEY_MODO_OSCURO = "modo_oscuro"
    }
    
    // Getters para obtener configuraciones (por defecto todas false)
    fun getRecordatorioAgua(): Boolean = prefs.getBoolean(KEY_RECORDATORIO_AGUA, false)
    fun getSeguimientoDieta(): Boolean = prefs.getBoolean(KEY_SEGUIMIENTO_DIETA, false)
    fun getPesajeAutomatico(): Boolean = prefs.getBoolean(KEY_PESAJE_AUTOMATICO, false)
    fun getModoOscuro(): Boolean = prefs.getBoolean(KEY_MODO_OSCURO, false)
    
    // Setters para guardar configuraciones
    fun setRecordatorioAgua(activo: Boolean) {
        prefs.edit().putBoolean(KEY_RECORDATORIO_AGUA, activo).apply()
    }
    
    fun setSeguimientoDieta(activo: Boolean) {
        prefs.edit().putBoolean(KEY_SEGUIMIENTO_DIETA, activo).apply()
    }
    
    fun setPesajeAutomatico(activo: Boolean) {
        prefs.edit().putBoolean(KEY_PESAJE_AUTOMATICO, activo).apply()
    }
    
    fun setModoOscuro(activo: Boolean) {
        prefs.edit().putBoolean(KEY_MODO_OSCURO, activo).apply()
    }
    
    // Método para obtener todas las configuraciones
    fun obtenerTodasLasConfiguraciones(): ConfiguracionesPerfil {
        return ConfiguracionesPerfil(
            recordatorioAgua = getRecordatorioAgua(),
            seguimientoDieta = getSeguimientoDieta(),
            pesajeAutomatico = getPesajeAutomatico(),
            modoOscuro = getModoOscuro()
        )
    }
    
    // Método para guardar todas las configuraciones
    fun guardarTodasLasConfiguraciones(configuraciones: ConfiguracionesPerfil) {
        prefs.edit().apply {
            putBoolean(KEY_RECORDATORIO_AGUA, configuraciones.recordatorioAgua)
            putBoolean(KEY_SEGUIMIENTO_DIETA, configuraciones.seguimientoDieta)
            putBoolean(KEY_PESAJE_AUTOMATICO, configuraciones.pesajeAutomatico)
            putBoolean(KEY_MODO_OSCURO, configuraciones.modoOscuro)
            apply()
        }
    }
    
    // Método para resetear todas las configuraciones
    fun resetearConfiguraciones() {
        prefs.edit().clear().apply()
    }
}

// Data class para configuraciones (movido aquí para reutilizar)
data class ConfiguracionesPerfil(
    val recordatorioAgua: Boolean = false,
    val seguimientoDieta: Boolean = false,
    val pesajeAutomatico: Boolean = false,
    val modoOscuro: Boolean = false
)
