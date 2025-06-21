package com.example.nutriton.ui.PlanComidas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.UiState
import com.example.nutriton.data.api.models.TipoDieta
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.ui.PlanComidas.model.PlanComida
import kotlinx.coroutines.launch

class PlanComidasViewModel : ViewModel() {

    private val repository = NutritionRepository.getInstance()

    private val _uiState = MutableLiveData<UiState<List<PlanComida>>>()
    val uiState: LiveData<UiState<List<PlanComida>>> = _uiState

    private val _selectedPlan = MutableLiveData<PlanComida?>()
    val selectedPlan: LiveData<PlanComida?> = _selectedPlan

    private val _planesComida = MutableLiveData<List<PlanComida>>()
    val planesComida: LiveData<List<PlanComida>> = _planesComida

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadPlanesComida()
    }

    private fun loadPlanesComida() {
        _uiState.value = UiState(isLoading = true)
        _isLoading.value = true

        viewModelScope.launch {
            when (val result = repository.getTiposDieta()) {
                is ApiResult.Success -> {
                    val planes = convertTiposDietaToPlanesComida(result.data)
                    _uiState.value = UiState(data = planes)
                    _planesComida.value = planes
                    _isLoading.value = false
                }
                is ApiResult.Error -> {
                    _uiState.value = UiState(error = "Error al cargar planes: ${result.exception.message}")
                    _errorMessage.value = "Error al cargar planes: ${result.exception.message}"
                    _isLoading.value = false
                }
                ApiResult.Loading -> {
                    _uiState.value = UiState(isLoading = true)
                    _isLoading.value = true
                }
            }
        }
    }    private fun convertTiposDietaToPlanesComida(tiposDieta: List<TipoDieta>): List<PlanComida> {
        return tiposDieta.map { tipoDieta ->
            PlanComida(
                id = tipoDieta.id,
                nombre = tipoDieta.nombre,
                descripcion = tipoDieta.descripcion ?: "Sin descripción disponible",
                // Usar imagen real si está disponible, sino usar placeholder
                imagenUrl = tipoDieta.imagen_url ?: "https://via.placeholder.com/300x200/2E7D32/FFFFFF?text=${tipoDieta.nombre.replace(" ", "+")}",
                categoria = "Plan Nutricional",
                duracion = "7 días",
                calorias = 2000,
                totalRecetas = tipoDieta.total_recetas ?: 0,
                tipoDieta = com.example.nutriton.ui.PlanComidas.model.TipoDieta(
                    id = tipoDieta.id,
                    nombre = tipoDieta.nombre,
                    descripcion = tipoDieta.descripcion ?: "Sin descripción disponible"
                )
            )
        }
    }

    fun selectPlan(plan: PlanComida) {
        _selectedPlan.value = plan
    }

    fun clearSelection() {
        _selectedPlan.value = null
    }

    fun refreshPlanes() {
        loadPlanesComida()
    }    fun getPlanByTipoDietaId(tipoDietaId: String): PlanComida? {
        return _uiState.value?.data?.find { it.id == tipoDietaId }
    }

    // Función para filtrar planes
    fun filterPlanes(filtro: String): List<PlanComida> {
        return _planesComida.value?.filter { 
            it.nombre.contains(filtro, ignoreCase = true) ||
            it.descripcion.contains(filtro, ignoreCase = true)        } ?: emptyList()
    }
    
    fun clearError() {
        _errorMessage.value = null
    }
}