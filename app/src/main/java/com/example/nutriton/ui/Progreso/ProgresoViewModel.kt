package com.example.nutriton.ui.Progreso

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.asLiveData
import com.example.nutriton.data.models.ProgresoUsuario
import com.example.nutriton.database.entities.ProgresoDiario
import com.example.nutriton.data.repository.NutritionRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class ProgresoViewModel(private val repository: NutritionRepository) : ViewModel() {

    private val fechaHoy = LocalDate.now()
    
    // Observar el progreso de hoy directamente desde la base de datos
    val progresoHoy: LiveData<ProgresoDiario?> = repository.observarProgresoHoy(fechaHoy).asLiveData()
    
    // Progreso combinado para mostrar en la UI
    private val _progresoUI = MutableLiveData<ProgresoUIState>()
    val progresoUI: LiveData<ProgresoUIState> = _progresoUI
    
    init {
        cargarProgresoCompleto()
    }

    /**
     * Carga el progreso completo (días anteriores + hoy)
     */
    private fun cargarProgresoCompleto() {
        viewModelScope.launch {
            try {
                val fechaInicio = fechaHoy.minusDays(4) // Últimos 5 días
                val progresoSemana = repository.obtenerProgresoRango(fechaInicio, fechaHoy)
                val rachaActual = repository.obtenerRachaActual(fechaHoy)
                
                // Crear lista de 5 días con su estado
                val diasEstado = mutableListOf<Boolean>()
                for (i in 0..4) {
                    val fecha = fechaInicio.plusDays(i.toLong())
                    val progresoDia = progresoSemana.find { it.fecha == fecha }
                    diasEstado.add(progresoDia?.dia_completado == true)
                }
                
                _progresoUI.value = ProgresoUIState(
                    diasCompletados = diasEstado,
                    rachaActual = rachaActual,
                    diaActual = 5 // Siempre el último día de los 5 mostrados
                )
                
            } catch (e: Exception) {
                // En caso de error, mostrar estado por defecto
                _progresoUI.value = ProgresoUIState()
            }
        }
    }

    /**
     * Marca una comida como completada
     */
    fun marcarComidaCompletada(tipoComida: TipoComida) {
        viewModelScope.launch {
            try {
                val tipoString = when (tipoComida) {
                    TipoComida.DESAYUNO -> "DESAYUNO"
                    TipoComida.ALMUERZO -> "ALMUERZO"
                    TipoComida.CENA -> "CENA"
                }
                
                repository.marcarComidaCompletada(fechaHoy, tipoString, true)
                cargarProgresoCompleto() // Recargar para actualizar la racha
                
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    /**
     * Desmarca una comida (útil si se marca por error)
     */
    fun desmarcarComidaCompletada(tipoComida: TipoComida) {
        viewModelScope.launch {
            try {
                val tipoString = when (tipoComida) {
                    TipoComida.DESAYUNO -> "DESAYUNO"
                    TipoComida.ALMUERZO -> "ALMUERZO"
                    TipoComida.CENA -> "CENA"
                }
                
                repository.marcarComidaCompletada(fechaHoy, tipoString, false)
                cargarProgresoCompleto()
                
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    /**
     * Incrementa el contador de vasos de agua
     */
    fun incrementarVasosAgua() {
        viewModelScope.launch {
            try {
                repository.incrementarVasoAgua(fechaHoy)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    /**
     * Decrementa el contador de vasos de agua
     */
    fun decrementarVasosAgua() {
        viewModelScope.launch {
            try {
                repository.decrementarVasoAgua(fechaHoy)
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    /**
     * Recarga todo el progreso
     */
    fun recargarProgreso() {
        cargarProgresoCompleto()
    }
}

/**
 * Estado de la UI para mostrar el progreso semanal
 */
data class ProgresoUIState(
    val diasCompletados: List<Boolean> = listOf(false, false, false, false, false),
    val rachaActual: Int = 0,
    val diaActual: Int = 5
)

enum class TipoComida {
    DESAYUNO, ALMUERZO, CENA
}