package com.example.nutriton.ui.Perfil.GestionarMetas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.databinding.FragmentGestionarmetasBinding
import com.example.nutriton.ui.Preferencias.PreferenciasViewModel
import com.example.nutriton.ui.Preferencias.PreferenciasViewModelFactory
import com.google.android.material.chip.Chip

class GestionarMetasFragment : Fragment() {

    private var _binding: FragmentGestionarmetasBinding? = null
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
        _binding = FragmentGestionarmetasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        // Observar objetivos
        viewModel.objetivos.observe(viewLifecycleOwner) { objetivos ->
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

        // Observar estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Si hay progressBar en el layout
            // binding.progressBar?.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observar mensajes
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajes()
            }
        }

        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                viewModel.limpiarMensajes()
            }
        }
    }

    private fun setupClickListeners() {
        // Usar solo el botón agregar objetivo para guardar/actualizar la meta
        binding.btnAgregarObjetivo.setOnClickListener {
            viewModel.guardarPreferencias()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}