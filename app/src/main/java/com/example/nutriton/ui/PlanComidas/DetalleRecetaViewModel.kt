package com.example.nutriton.ui.PlanComidas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.UiState
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.data.repository.NutritionRepository
import kotlinx.coroutines.launch

class DetalleRecetaViewModel : ViewModel() {

    private val repository = NutritionRepository.getInstance()

    private val _uiState = MutableLiveData<UiState<Receta>>()
    val uiState: LiveData<UiState<Receta>> = _uiState

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadReceta(recetaId: Int) {
        _uiState.value = UiState(isLoading = true)
        _isLoading.value = true

        viewModelScope.launch {
            try {
                when (val result = repository.getRecetaById(recetaId)) {
                    is ApiResult.Success -> {
                        _uiState.value = UiState(data = result.data)
                    }
                    is ApiResult.Error -> {
                        _uiState.value = UiState(error = "Error al cargar receta: ${result.exception.message}")
                        _errorMessage.value = "Error al cargar receta: ${result.exception.message}"
                    }
                    ApiResult.Loading -> {
                        _uiState.value = UiState(isLoading = true)
                    }
                }

                // Si no hay implementación de API todavía, mostrar mensaje informativo
                _errorMessage.value = "Integración con API pendiente. ID de receta: $recetaId"
                
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión: ${e.message}"
                _uiState.value = UiState(error = "Error de conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshReceta(recetaId: Int) {
        loadReceta(recetaId)
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
