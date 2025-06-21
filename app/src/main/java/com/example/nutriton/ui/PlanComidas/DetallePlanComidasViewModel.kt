package com.example.nutriton.ui.PlanComidas

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutriton.data.api.ApiResult
import com.example.nutriton.data.api.UiState
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.ui.PlanComidas.model.ComidaCompleta
import com.example.nutriton.ui.PlanComidas.model.DiaPlan
import com.example.nutriton.ui.PlanComidas.model.InfoNutricional
import com.example.nutriton.ui.PlanComidas.model.PlanComida
import kotlinx.coroutines.launch

class DetallePlanComidasViewModel : ViewModel() {

    private val repository = NutritionRepository.getInstance()

    private val _planActual = MutableLiveData<PlanComida?>()
    val planActual: LiveData<PlanComida?> = _planActual

    private val _diaSeleccionado = MutableLiveData<Int>()
    val diaSeleccionado: LiveData<Int> = _diaSeleccionado

    private val _diasPlan = MutableLiveData<List<DiaPlan>>()
    val diasPlan: LiveData<List<DiaPlan>> = _diasPlan

    private val _uiState = MutableLiveData<UiState<List<ComidaCompleta>>>()
    val uiState: LiveData<UiState<List<ComidaCompleta>>> = _uiState

    private val _comidasDesayuno = MutableLiveData<List<ComidaCompleta>>()
    val comidasDesayuno: LiveData<List<ComidaCompleta>> = _comidasDesayuno

    private val _comidasAlmuerzo = MutableLiveData<List<ComidaCompleta>>()
    val comidasAlmuerzo: LiveData<List<ComidaCompleta>> = _comidasAlmuerzo

    private val _comidasCena = MutableLiveData<List<ComidaCompleta>>()
    val comidasCena: LiveData<List<ComidaCompleta>> = _comidasCena

    init {
        _diaSeleccionado.value = 1
        initializeDiasPlan()
    }

    private fun initializeDiasPlan() {
        // Crear 7 días de plan
        val dias = (1..7).map { dia ->
            DiaPlan(
                id = dia,
                numeroDia = dia,
                planId = 0,
                nombreDia = when (dia) {
                    1 -> "Lun"
                    2 -> "Mar"
                    3 -> "Mié"
                    4 -> "Jue"
                    5 -> "Vie"
                    6 -> "Sáb"
                    7 -> "Dom"
                    else -> "Día $dia"
                }
            )
        }
        _diasPlan.value = dias
    }    fun loadPlan(planId: Int) {
        // Solo cargar si no tenemos ya un plan cargado
        if (_planActual.value == null) {
            loadComidasPorDia(_diaSeleccionado.value ?: 1)
        }
    }

    fun setPlan(plan: PlanComida) {
        _planActual.value = plan
        // Solo cargar comidas una vez cuando se establece el plan
        loadComidasPorDia(_diaSeleccionado.value ?: 1)
    }

    fun selectDia(numeroDia: Int) {
        _diaSeleccionado.value = numeroDia
        // No recargar datos, solo reorganizar los existentes
        organizarComidasPorDia(getCurrentComidas())
    }

    private fun loadComidasPorDia(numeroDia: Int) {
        // Evitar múltiples llamadas si ya está cargando
        if (_uiState.value?.isLoading == true) return
        
        _uiState.value = UiState(isLoading = true)
        
        val planActual = _planActual.value
        if (planActual == null) {
            _uiState.value = UiState(error = "No hay plan seleccionado")
            return
        }

        viewModelScope.launch {
            try {                // Usar recetas filtradas por tipo de dieta en lugar de cargar todas
                val tipoDietaNombre = planActual.tipoDieta?.nombre
                println("DEBUG ViewModel: tipoDietaNombre = '$tipoDietaNombre'")
                println("DEBUG ViewModel: planActual = ${planActual.nombre}, tipoDieta = ${planActual.tipoDieta}")
                println("DEBUG ViewModel: Llamando repository.getRecetasFiltradas(tipoDieta='$tipoDietaNombre')")
                
                when (val result = repository.getRecetasFiltradas(tipoDieta = tipoDietaNombre)) {
                    is ApiResult.Success -> {
                        val recetas = result.data
                        println("DEBUG ViewModel: Recetas obtenidas correctamente: ${recetas.size}")
                        recetas.forEachIndexed { index, receta ->
                            println("DEBUG ViewModel: Receta $index: id=${receta.id}, nombre=${receta.nombre}")
                        }
                        val comidas = convertRecetasToComidas(recetas, numeroDia)
                        println("DEBUG ViewModel: Comidas convertidas: ${comidas.size}")
                        organizarComidasPorCategoria(comidas)
                        _uiState.value = UiState(data = comidas)
                        println("DEBUG ViewModel: Estado UI actualizado con ${comidas.size} comidas")
                    }
                    is ApiResult.Error -> {
                        println("DEBUG ViewModel: Error al cargar recetas: ${result.exception.message}")
                        _uiState.value = UiState(error = "Error al cargar comidas: ${result.exception.message}")
                    }
                    ApiResult.Loading -> {
                        _uiState.value = UiState(isLoading = true)
                    }
                }
            } catch (e: Exception) {
                println("DEBUG: Exception: ${e.message}")
                e.printStackTrace()
                _uiState.value = UiState(error = "Error inesperado: ${e.message}")
            }
        }
    }    private fun convertRecetasToComidas(recetas: List<Receta>, dia: Int): List<ComidaCompleta> {
        // Limitar el número de recetas para evitar problemas de memoria
        val recetasLimitadas = recetas.take(9) // 3 por cada comida máximo
        
        println("DEBUG: convertRecetasToComidas - Procesando ${recetasLimitadas.size} recetas")
        
        return recetasLimitadas.mapIndexed { index, receta ->
            val categoria = when (index % 3) {
                0 -> "Desayuno"
                1 -> "Almuerzo"
                else -> "Cena"
            }
            
            val calorias = try {
                receta.informacion_nutricional?.calorias ?: 250
            } catch (e: Exception) {
                println("DEBUG: Error al obtener calorías para receta ${receta.id}: ${e.message}")
                250
            }
            
            val imagenUrl = receta.imagen_url ?: "https://via.placeholder.com/300x200/4CAF50/FFFFFF?text=${receta.nombre.replace(" ", "+")}"
            
            val comida = ComidaCompleta(
                id = receta.id,
                nombre = receta.nombre,
                descripcion = receta.descripcion,
                imagenUrl = imagenUrl,
                categoria = categoria,
                calorias = calorias,
                tiempoCoccion = receta.tiempo_preparacion,
                ingredientes = receta.ingredientes,
                instrucciones = receta.instrucciones,
                informacionNutricional = receta.informacion_nutricional?.let { info ->
                    InfoNutricional(
                        calorias = info.calorias,
                        proteinas = info.proteinas?.toFloatOrNull() ?: 0f,
                        carbohidratos = info.carbohidratos?.toFloatOrNull() ?: 0f,
                        grasas = info.grasas?.toFloatOrNull() ?: 0f
                    )
                }
            )
            
            println("DEBUG: Creada comida: ${comida.nombre}, categoria: ${comida.categoria}, id: ${comida.id}")
            comida
        }
    }

    private fun getCurrentComidas(): List<ComidaCompleta> {
        return _uiState.value?.data ?: emptyList()
    }    private fun organizarComidasPorCategoria(comidas: List<ComidaCompleta>) {
        val desayunos = comidas.filter { it.categoria == "Desayuno" }
        val almuerzos = comidas.filter { it.categoria == "Almuerzo" }
        val cenas = comidas.filter { it.categoria == "Cena" }
        
        println("DEBUG: organizarComidasPorCategoria - Total comidas: ${comidas.size}")
        println("DEBUG: Desayunos: ${desayunos.size}, Almuerzos: ${almuerzos.size}, Cenas: ${cenas.size}")
        
        _comidasDesayuno.value = desayunos
        _comidasAlmuerzo.value = almuerzos
        _comidasCena.value = cenas
    }

    private fun organizarComidasPorDia(comidas: List<ComidaCompleta>) {
        // Filtra las comidas según el día seleccionado y las organiza por categoría
        val diaActual = _diaSeleccionado.value ?: 1

        // En una implementación real, podrías filtrar por día usando alguna propiedad
        // Por ahora, simplemente pasamos las comidas al organizador por categoría
        organizarComidasPorCategoria(comidas)

        // Actualiza el estado de UI
        _uiState.value = UiState(data = comidas)
    }

    fun refreshComidas() {
        // Solo refrescar si tenemos un plan
        val plan = _planActual.value
        if (plan != null) {
            loadComidasPorDia(_diaSeleccionado.value ?: 1)
        }
    }
}
