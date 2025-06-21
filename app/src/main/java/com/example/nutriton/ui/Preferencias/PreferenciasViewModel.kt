package com.example.nutriton.ui.Preferencias

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.models.Objetivo
import com.example.nutriton.data.api.models.TipoDieta
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.database.entities.PreferenciasUsuario
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class PreferenciasViewModel(private val repository: NutritionRepository) : ViewModel() {

    private val _preferencias = MutableLiveData<PreferenciasUsuario>()
    val preferencias: LiveData<PreferenciasUsuario> = _preferencias

    private val _tiposDieta = MutableLiveData<List<TipoDieta>>()
    val tiposDieta: LiveData<List<TipoDieta>> = _tiposDieta

    private val _objetivos = MutableLiveData<List<Objetivo>>()
    val objetivos: LiveData<List<Objetivo>> = _objetivos

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage

    init {
        cargarDatos()
    }

    /**
     * Carga todos los datos necesarios
     */
    private fun cargarDatos() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Cargar preferencias locales
            cargarPreferenciasLocales()
            
            // Cargar opciones disponibles
            cargarTiposDieta()
            cargarObjetivos()
            
            _isLoading.value = false
        }
    }

    /**
     * Carga las preferencias almacenadas localmente
     */
    private suspend fun cargarPreferenciasLocales() {
        try {
            val prefs = repository.obtenerPreferenciasLocal()
            if (prefs != null) {
                _preferencias.value = prefs
            } else {
                // Crear preferencias por defecto
                val defaultPrefs = PreferenciasUsuario(
                    id = 1,
                    tipo_dieta_id = null,
                    objetivo_id = null,
                    porcentaje_proteinas = 25,
                    porcentaje_carbohidratos = 50,
                    porcentaje_grasas = 25,
                    nivel_actividad = "Moderado",
                    peso_objetivo = null,
                    fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                )
                _preferencias.value = defaultPrefs
            }
        } catch (e: Exception) {
            _errorMessage.value = "Error al cargar preferencias: ${e.message}"
        }
    }

    /**
     * Carga los tipos de dieta disponibles
     */
    private suspend fun cargarTiposDieta() {
        when (val result = repository.getTiposDieta()) {
            is ApiResult.Success -> {
                _tiposDieta.value = result.data
            }
            is ApiResult.Error -> {
                _errorMessage.value = "Error al cargar tipos de dieta: ${result.exception.message}"
            }
            is ApiResult.Loading -> {
                // Estado de carga ya manejado con _isLoading
            }
        }
    }

    /**
     * Carga los objetivos disponibles
     */
    private suspend fun cargarObjetivos() {
        when (val result = repository.getObjetivos()) {
            is ApiResult.Success -> {
                _objetivos.value = result.data
            }
            is ApiResult.Error -> {
                _errorMessage.value = "Error al cargar objetivos: ${result.exception.message}"
            }
            is ApiResult.Loading -> {
                // Estado de carga ya manejado con _isLoading
            }
        }
    }

    /**
     * Actualiza el tipo de dieta seleccionado
     */
    fun actualizarTipoDieta(tipoDietaId: Int) {
        val currentPrefs = _preferencias.value ?: return
        val updatedPrefs = currentPrefs.copy(
            tipo_dieta_id = tipoDietaId,
            fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        _preferencias.value = updatedPrefs
    }

    /**
     * Actualiza el objetivo seleccionado
     */
    fun actualizarObjetivo(objetivoId: Int) {
        val currentPrefs = _preferencias.value ?: return
        val updatedPrefs = currentPrefs.copy(
            objetivo_id = objetivoId,
            fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        _preferencias.value = updatedPrefs
    }

    /**
     * Actualiza los porcentajes de macronutrientes
     */
    fun actualizarMacronutrientes(proteinas: Int, carbohidratos: Int, grasas: Int) {
        // Validar que sumen 100%
        if (proteinas + carbohidratos + grasas != 100) {
            _errorMessage.value = "Los porcentajes deben sumar 100%"
            return
        }

        val currentPrefs = _preferencias.value ?: return
        val updatedPrefs = currentPrefs.copy(
            porcentaje_proteinas = proteinas,
            porcentaje_carbohidratos = carbohidratos,
            porcentaje_grasas = grasas,
            fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        _preferencias.value = updatedPrefs
    }

    /**
     * Actualiza el nivel de actividad
     */
    fun actualizarNivelActividad(nivelActividad: String) {
        val currentPrefs = _preferencias.value ?: return
        val updatedPrefs = currentPrefs.copy(
            nivel_actividad = nivelActividad,
            fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        _preferencias.value = updatedPrefs
    }

    /**
     * Actualiza el peso objetivo
     */
    fun actualizarPesoObjetivo(peso: Float?) {
        val currentPrefs = _preferencias.value ?: return
        val updatedPrefs = currentPrefs.copy(
            peso_objetivo = peso,
            fecha_actualizacion = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        )
        _preferencias.value = updatedPrefs
    }

    /**
     * Guarda las preferencias localmente y sincroniza con el servidor
     */
    fun guardarPreferencias() {
        viewModelScope.launch {
            _isLoading.value = true
            
            try {
                val prefs = _preferencias.value
                if (prefs != null) {
                    // Guardar localmente
                    repository.guardarPreferenciasLocal(prefs)
                    
                    // Sincronizar con servidor
                    when (val result = repository.sincronizarPreferencias()) {
                        is ApiResult.Success -> {
                            _successMessage.value = "Preferencias guardadas correctamente"
                        }
                        is ApiResult.Error -> {
                            _successMessage.value = "Preferencias guardadas localmente (sin conexión)"
                        }
                        is ApiResult.Loading -> {
                            // Estado de carga ya manejado con _isLoading
                        }
                    }
                } else {
                    _errorMessage.value = "No hay preferencias para guardar"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar preferencias: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Sincroniza preferencias con el servidor
     */
    fun sincronizar() {
        viewModelScope.launch {
            _isLoading.value = true
            
            // Intentar descargar del servidor primero
            when (val result = repository.descargarPreferencias()) {
                is ApiResult.Success -> {
                    _successMessage.value = result.data
                    cargarPreferenciasLocales()
                }
                is ApiResult.Error -> {
                    // Si falla la descarga, intentar subir las locales
                    when (val syncResult = repository.sincronizarPreferencias()) {
                        is ApiResult.Success -> {
                            _successMessage.value = syncResult.data
                        }
                        is ApiResult.Error -> {
                            _errorMessage.value = "Sin conexión - trabajando offline"
                        }
                        is ApiResult.Loading -> {
                            // Estado de carga ya manejado con _isLoading
                        }
                    }
                }
                is ApiResult.Loading -> {
                    // Estado de carga ya manejado con _isLoading
                }
            }
            
            _isLoading.value = false
        }
    }

    /**
     * Limpia los mensajes de error y éxito
     */
    fun limpiarMensajes() {
        _errorMessage.value = ""
        _successMessage.value = ""
    }
}
