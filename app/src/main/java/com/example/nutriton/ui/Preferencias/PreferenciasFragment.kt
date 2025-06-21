package com.example.nutriton.ui.Preferencias

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.databinding.FragmentPreferenciasBinding
import com.google.android.material.chip.Chip

class PreferenciasFragment : Fragment() {

    private var _binding: FragmentPreferenciasBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreferenciasViewModel by viewModels {
        PreferenciasViewModelFactory(
            NutritionRepository.getInstance(requireContext())
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreferenciasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        // Observar tipos de dieta
        viewModel.tiposDieta.observe(viewLifecycleOwner) { tiposDieta ->
            setupTiposDietaChips(tiposDieta)
        }

        // Observar objetivos
        viewModel.objetivos.observe(viewLifecycleOwner) { objetivos ->
            setupObjetivosChips(objetivos)
        }

        // Observar preferencias actuales
        viewModel.preferencias.observe(viewLifecycleOwner) { preferencias ->
            updateUI(preferencias)
        }

        // Observar estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnGuardar.isEnabled = !isLoading
        }

        // Observar mensajes de error
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajes()
            }
        }

        // Observar mensajes de éxito
        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.limpiarMensajes()
            }
        }
    }

    private fun setupTiposDietaChips(tiposDieta: List<com.example.nutriton.data.api.models.TipoDieta>) {
        binding.chipGroupTiposDieta.removeAllViews()
        
        tiposDieta.forEach { tipoDieta ->
            val chip = Chip(context).apply {
                text = tipoDieta.nombre
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.actualizarTipoDieta(tipoDieta.id.toInt())
                    }
                }
            }
            binding.chipGroupTiposDieta.addView(chip)
        }
    }

    private fun setupObjetivosChips(objetivos: List<com.example.nutriton.data.api.models.Objetivo>) {
        binding.chipGroupObjetivos.removeAllViews()
        
        objetivos.forEach { objetivo ->
            val chip = Chip(context).apply {
                text = objetivo.nombre
                isCheckable = true
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.actualizarObjetivo(objetivo.id)
                    }
                }
            }
            binding.chipGroupObjetivos.addView(chip)
        }
    }

    private fun setupClickListeners() {
        // Nivel de actividad
        binding.chipGroupNivelActividad.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val selectedChip = group.findViewById<Chip>(checkedIds[0])
                viewModel.actualizarNivelActividad(selectedChip.text.toString())
            }
        }

        // Botón guardar
        binding.btnGuardar.setOnClickListener {
            // Actualizar macronutrientes si han cambiado
            val proteinas = binding.sliderProteinas.value.toInt()
            val carbohidratos = binding.sliderCarbohidratos.value.toInt()
            val grasas = binding.sliderGrasas.value.toInt()
            
            if (proteinas + carbohidratos + grasas == 100) {
                viewModel.actualizarMacronutrientes(proteinas, carbohidratos, grasas)
            }
            
            // Actualizar peso objetivo si hay valor
            val pesoTexto = binding.etPesoObjetivo.text.toString()
            val peso = if (pesoTexto.isNotEmpty()) pesoTexto.toFloatOrNull() else null
            viewModel.actualizarPesoObjetivo(peso)
            
            // Guardar todas las preferencias
            viewModel.guardarPreferencias()
        }

        // Botón sincronizar
        binding.btnSincronizar.setOnClickListener {
            viewModel.sincronizar()
        }

        // Sliders de macronutrientes
        binding.sliderProteinas.addOnChangeListener { _, value, _ ->
            binding.tvProteinas.text = "Proteínas: ${value.toInt()}%"
            ajustarOtrosSliders(binding.sliderProteinas, value)
        }

        binding.sliderCarbohidratos.addOnChangeListener { _, value, _ ->
            binding.tvCarbohidratos.text = "Carbohidratos: ${value.toInt()}%"
            ajustarOtrosSliders(binding.sliderCarbohidratos, value)
        }

        binding.sliderGrasas.addOnChangeListener { _, value, _ ->
            binding.tvGrasas.text = "Grasas: ${value.toInt()}%"
            ajustarOtrosSliders(binding.sliderGrasas, value)
        }
    }

    private fun ajustarOtrosSliders(sliderCambiado: com.google.android.material.slider.Slider, nuevoValor: Float) {
        val total = 100f
        val restante = total - nuevoValor
        
        when (sliderCambiado) {
            binding.sliderProteinas -> {
                val carbos = binding.sliderCarbohidratos.value
                val grasas = binding.sliderGrasas.value
                val sumaOtros = carbos + grasas
                
                if (sumaOtros > 0) {
                    val factorAjuste = restante / sumaOtros
                    binding.sliderCarbohidratos.value = carbos * factorAjuste
                    binding.sliderGrasas.value = grasas * factorAjuste
                }
            }
            binding.sliderCarbohidratos -> {
                val proteinas = binding.sliderProteinas.value
                val grasas = binding.sliderGrasas.value
                val sumaOtros = proteinas + grasas
                
                if (sumaOtros > 0) {
                    val factorAjuste = restante / sumaOtros
                    binding.sliderProteinas.value = proteinas * factorAjuste
                    binding.sliderGrasas.value = grasas * factorAjuste
                }
            }
            binding.sliderGrasas -> {
                val proteinas = binding.sliderProteinas.value
                val carbos = binding.sliderCarbohidratos.value
                val sumaOtros = proteinas + carbos
                
                if (sumaOtros > 0) {
                    val factorAjuste = restante / sumaOtros
                    binding.sliderProteinas.value = proteinas * factorAjuste
                    binding.sliderCarbohidratos.value = carbos * factorAjuste
                }
            }
        }
    }

    private fun updateUI(preferencias: com.example.nutriton.database.entities.PreferenciasUsuario) {
        // Actualizar sliders de macronutrientes
        binding.sliderProteinas.value = preferencias.porcentaje_proteinas.toFloat()
        binding.sliderCarbohidratos.value = preferencias.porcentaje_carbohidratos.toFloat()
        binding.sliderGrasas.value = preferencias.porcentaje_grasas.toFloat()
        
        // Actualizar texto de porcentajes
        binding.tvProteinas.text = "Proteínas: ${preferencias.porcentaje_proteinas}%"
        binding.tvCarbohidratos.text = "Carbohidratos: ${preferencias.porcentaje_carbohidratos}%"
        binding.tvGrasas.text = "Grasas: ${preferencias.porcentaje_grasas}%"
        
        // Actualizar peso objetivo
        binding.etPesoObjetivo.setText(preferencias.peso_objetivo?.toString() ?: "")
        
        // Marcar tipo de dieta seleccionado
        preferencias.tipo_dieta_id?.let { tipoDietaId ->
            for (i in 0 until binding.chipGroupTiposDieta.childCount) {
                val chip = binding.chipGroupTiposDieta.getChildAt(i) as Chip
                chip.isChecked = false
            }
            // Aquí necesitarías encontrar el chip correcto basado en el ID
        }
        
        // Marcar objetivo seleccionado
        preferencias.objetivo_id?.let { objetivoId ->
            for (i in 0 until binding.chipGroupObjetivos.childCount) {
                val chip = binding.chipGroupObjetivos.getChildAt(i) as Chip
                chip.isChecked = false
            }
            // Aquí necesitarías encontrar el chip correcto basado en el ID
        }
        
        // Marcar nivel de actividad
        when (preferencias.nivel_actividad) {
            "Sedentario" -> binding.chipSedentario.isChecked = true
            "Ligero" -> binding.chipLigero.isChecked = true
            "Moderado" -> binding.chipModerado.isChecked = true
            "Intenso" -> binding.chipIntenso.isChecked = true
            "Muy Intenso" -> binding.chipMuyIntenso.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class PreferenciasViewModelFactory(
    private val repository: NutritionRepository
) : ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferenciasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreferenciasViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
