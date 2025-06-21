package com.example.nutriton.ui.Progreso

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.R
import com.example.nutriton.databinding.FragmentProgresoBinding
import com.example.nutriton.database.NutritionDatabase
import com.example.nutriton.data.repository.NutritionRepository

class ProgresoFragment : Fragment() {

    private var _binding: FragmentProgresoBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: ProgresoViewModel
    private lateinit var repository: NutritionRepository
    
    // Referencias a los elementos de la UI
    private lateinit var vasosAgua: List<TextView>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProgresoBinding.inflate(inflater, container, false)
        
        // Inicializar repository y ViewModel
        repository = NutritionRepository.getInstance(requireContext())

        val factory = ProgresoViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProgresoViewModel::class.java]
        
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupUI()
        observeViewModel()
        setupClickListeners()
    }

    private fun setupUI() {
        // Inicializar las referencias a los vasos de agua
        vasosAgua = listOf(
            binding.vaso1,
            binding.vaso2,
            binding.vaso3,
            binding.vaso4,
            binding.vaso5,
            binding.vaso6
        )
    }

    private fun observeViewModel() {
        // Observar el progreso de hoy
        viewModel.progresoHoy.observe(viewLifecycleOwner) { progreso ->
            progreso?.let {
                updateMealProgress(it)
                updateWaterProgress(it.vasos_agua_consumidos, it.vasos_agua_objetivo)
            }
        }
        
        // Observar el progreso semanal
        viewModel.progresoUI.observe(viewLifecycleOwner) { uiState ->
            updateDaysProgress(uiState.diasCompletados)
            updateStreakCounter(uiState.rachaActual)
        }
    }

    private fun setupClickListeners() {
        // Botones de comidas
        binding.layoutDesayuno.setOnClickListener {
            toggleMealCompletion(TipoComida.DESAYUNO)
        }
        
        binding.layoutAlmuerzo.setOnClickListener {
            toggleMealCompletion(TipoComida.ALMUERZO)
        }
        
        binding.layoutCena.setOnClickListener {
            toggleMealCompletion(TipoComida.CENA)
        }
        
        // Botones de agua
        binding.btnAddAgua.setOnClickListener {
            viewModel.incrementarVasosAgua()
        }
        
        binding.btnRemoveAgua.setOnClickListener {
            viewModel.decrementarVasosAgua()
        }
    }

    private fun toggleMealCompletion(tipoComida: TipoComida) {
        val progresoActual = viewModel.progresoHoy.value
        val yaCompletada = when (tipoComida) {
            TipoComida.DESAYUNO -> progresoActual?.desayuno_completado == true
            TipoComida.ALMUERZO -> progresoActual?.almuerzo_completado == true
            TipoComida.CENA -> progresoActual?.cena_completada == true
        }
        
        if (yaCompletada) {
            viewModel.desmarcarComidaCompletada(tipoComida)
        } else {
            viewModel.marcarComidaCompletada(tipoComida)
        }
    }

    private fun updateMealProgress(progreso: com.example.nutriton.database.entities.ProgresoDiario) {
        // Actualizar UI de desayuno
        binding.checkDesayuno.visibility = if (progreso.desayuno_completado) View.VISIBLE else View.INVISIBLE
        
        // Actualizar UI de almuerzo  
        binding.checkAlmuerzo.visibility = if (progreso.almuerzo_completado) View.VISIBLE else View.INVISIBLE
        
        // Actualizar UI de cena
        binding.checkCena.visibility = if (progreso.cena_completada) View.VISIBLE else View.INVISIBLE
    }

    private fun updateWaterProgress(vasosConsumidos: Int, vasosObjetivo: Int) {
        // Actualizar vasos de agua
        vasosAgua.forEachIndexed { index, vaso ->
            if (index < vasosConsumidos) {
                vaso.alpha = 1.0f // Vaso lleno
                vaso.text = "🥤"
            } else {
                vaso.alpha = 0.3f // Vaso vacío
                vaso.text = "🥛"
            }
        }
        
        // Actualizar contador de agua
        binding.contadorAgua.text = "$vasosConsumidos/$vasosObjetivo"
    }

    private fun updateDaysProgress(diasCompletados: List<Boolean>) {
        val diasViews = listOf(
            Triple(binding.circleDia1, binding.checkDia1, binding.textDia1),
            Triple(binding.circleDia2, binding.checkDia2, binding.textDia2),
            Triple(binding.circleDia3, binding.checkDia3, binding.textDia3),
            Triple(binding.circleDia4, binding.checkDia4, binding.textDia4),
            Triple(binding.circleDia5, binding.checkDia5, binding.textDia5)
        )
        
        diasCompletados.forEachIndexed { index, completado ->
            if (index < diasViews.size) {
                val (circle, check, text) = diasViews[index]
                if (completado) {
                    circle.setBackgroundResource(R.drawable.circle_shape)
                    check.visibility = View.VISIBLE
                    text.setTextColor(resources.getColor(android.R.color.white, null))
                } else {
                    circle.setBackgroundResource(R.drawable.circle_shape_gray)
                    check.visibility = View.GONE
                    text.setTextColor(resources.getColor(R.color.gray_text, null))
                }
            }
        }
    }

    private fun updateStreakCounter(racha: Int) {
        // Si tienes un TextView para mostrar la racha, descomenta y ajusta:
        // binding.contadorRacha.text = "$racha días"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}