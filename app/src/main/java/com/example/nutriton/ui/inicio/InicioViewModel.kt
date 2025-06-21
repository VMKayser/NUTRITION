package com.example.nutriton.ui.inicio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.data.repository.NutritionRepository
import kotlinx.coroutines.launch

class InicioViewModel(private val repository: NutritionRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Comida del Día"
    }
    val text: LiveData<String> = _text

    private val _recetaDelDia = MutableLiveData<Receta?>()
    val recetaDelDia: LiveData<Receta?> = _recetaDelDia

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        cargarRecetaDelDia()
    }

    /**
     * Cargar una receta aleatoria para mostrar como "receta del día"
     */
    fun cargarRecetaDelDia() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                // Cargar todas las recetas sin filtros para obtener una aleatoria
                when (val result = repository.getRecetasFiltradas(
                    categoria = null,
                    tipoDieta = null,
                    objetivo = null,
                    caloriasMin = null,
                    caloriasMax = null
                )) {
                    is ApiResult.Success -> {
                        if (result.data.isNotEmpty()) {
                            // Seleccionar una receta aleatoria
                            val recetaAleatoria = result.data.random()
                            _recetaDelDia.value = recetaAleatoria
                        } else {
                            _errorMessage.value = "No se encontraron recetas"
                        }
                        _isLoading.value = false
                    }
                    is ApiResult.Error -> {
                        _errorMessage.value = "Error al cargar receta: ${result.exception.message}"
                        _isLoading.value = false
                    }
                    ApiResult.Loading -> {
                        _isLoading.value = true
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    /**
     * Limpiar mensajes de error
     */
    fun limpiarError() {
        _errorMessage.value = null
    }
}