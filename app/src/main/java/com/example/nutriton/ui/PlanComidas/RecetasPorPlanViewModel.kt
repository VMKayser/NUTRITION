package com.example.nutriton.ui.PlanComidas

// Este archivo ya no se usa - se mantiene el DetallePlanComidasViewModel con el diseño original

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.data.repository.NutritionRepository
import kotlinx.coroutines.launch

class RecetasPorPlanViewModel(private val repository: NutritionRepository) : ViewModel() {

    private val _recetas = MutableLiveData<List<Receta>>()
    val recetas: LiveData<List<Receta>> = _recetas

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _planNombre = MutableLiveData<String>()
    val planNombre: LiveData<String> = _planNombre

    /**
     * Cargar recetas filtradas por tipo de dieta/plan
     */
    fun cargarRecetasPorPlan(tipoDietaNombre: String, planNombre: String? = null) {
        _isLoading.value = true
        _errorMessage.value = null
        _planNombre.value = planNombre ?: tipoDietaNombre

        viewModelScope.launch {
            try {
                println("DEBUG: Cargando recetas para tipo de dieta: $tipoDietaNombre")
                
                when (val result = repository.getRecetasFiltradas(
                    categoria = null,
                    tipoDieta = tipoDietaNombre,
                    objetivo = null,
                    caloriasMin = null,
                    caloriasMax = null
                )) {
                    is ApiResult.Success -> {
                        _recetas.value = result.data
                        _isLoading.value = false
                        println("DEBUG: Recetas cargadas exitosamente: ${result.data.size} recetas")
                    }
                    is ApiResult.Error -> {
                        _errorMessage.value = "Error al cargar recetas: ${result.exception.message}"
                        _recetas.value = emptyList()
                        _isLoading.value = false
                        println("DEBUG: Error al cargar recetas: ${result.exception.message}")
                    }
                    ApiResult.Loading -> {
                        _isLoading.value = true
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
                _recetas.value = emptyList()
                _isLoading.value = false
                println("DEBUG: Exception al cargar recetas: ${e.message}")
            }
        }
    }

    /**
     * Recargar recetas
     */
    fun recargarRecetas(tipoDietaNombre: String, planNombre: String? = null) {
        cargarRecetasPorPlan(tipoDietaNombre, planNombre)
    }

    /**
     * Limpiar mensajes de error
     */
    fun limpiarError() {
        _errorMessage.value = null
    }
}