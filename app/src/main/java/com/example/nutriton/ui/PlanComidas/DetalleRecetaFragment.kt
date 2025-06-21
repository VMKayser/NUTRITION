package com.example.nutriton.ui.PlanComidas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nutriton.databinding.FragmentDetallesrecetaBinding
import com.example.nutriton.data.api.models.Receta
import com.example.nutriton.util.ImageUtils

class DetalleRecetaFragment : Fragment() {

    private var _binding: FragmentDetallesrecetaBinding? = null
    private val binding get() = _binding!!
    private lateinit var detalleRecetaViewModel: DetalleRecetaViewModel
    
    private var recetaId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtener ID de la receta desde argumentos
        arguments?.let {
            recetaId = it.getInt("recetaId", -1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detalleRecetaViewModel = ViewModelProvider(this)[DetalleRecetaViewModel::class.java]
        _binding = FragmentDetallesrecetaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupObservers()
        
        // Cargar receta si se proporcionó un ID válido
        if (recetaId != -1) {
            detalleRecetaViewModel.loadReceta(recetaId)
        }
        
        return root
    }

    private fun setupObservers() {
        detalleRecetaViewModel.uiState.observe(viewLifecycleOwner) { state ->
            // Manejar loading
            binding.progressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE
            binding.scrollViewReceta.visibility = if (state.isLoading) View.GONE else View.VISIBLE
            
            // Manejar datos
            state.data?.let { receta ->
                bindRecetaData(receta)
            }
            
            // Manejar errores
            state.error?.let { error ->
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
        }

        // Observar estado de carga
        detalleRecetaViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // TODO: Mostrar/ocultar indicador de carga si es necesario
        }

        // Observar errores
        detalleRecetaViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                detalleRecetaViewModel.clearError()
            }
        }
    }

    private fun bindRecetaData(receta: Receta) {
        binding.apply {
            // Cargar imagen usando Glide
            if (receta.imagen_url != null) {
                ImageUtils.loadImage(
                    ivRecetaImagen,
                    receta.imagen_url,
                    receta.nombre
                )
            } else {
                // Cargar imagen de placeholder si no hay imagen disponible
                ImageUtils.loadImage(
                    ivRecetaImagen,
                    null,
                    receta.nombre
                )
            }

            // Información básica
            tvNombreReceta.text = receta.nombre

            // Porciones (valor por defecto si no está disponible)
            tvPorciones.text = "1 Porción" // Receta no tiene porciones, usamos valor por defecto

            // Información nutricional
            if (receta.informacion_nutricional != null && receta.informacion_nutricional.calorias > 0) {
                tvCalorias.text = "${receta.informacion_nutricional.calorias} Cal"
            } else {
                tvCalorias.text = "- Cal"
            }

            // Ingredientes - formatear como lista con viñetas
            val ingredientesFormateados = formatearIngredientes(receta.ingredientes)
            tvIngredientes.text = ingredientesFormateados

            // Instrucciones - formatear como pasos numerados
            val instruccionesFormateadas = formatearInstrucciones(receta.instrucciones)
            tvPreparacion.text = instruccionesFormateadas
        }
    }

    private fun formatearIngredientes(ingredientes: String?): String {
        if (ingredientes.isNullOrEmpty()) return "No hay ingredientes disponibles"
        
        // Si ya viene formateado con viñetas o números, devolverlo tal como está
        if (ingredientes.contains("•") || ingredientes.contains("*") || 
            ingredientes.matches(Regex(".*\\d+\\.-.*"))) {
            return ingredientes
        }
        
        // Si no, intentar formatear split por comas o punto y coma
        val items = ingredientes.split(Regex("[,;]")).map { it.trim() }
        return items.joinToString("\n") { "• $it" }
    }

    private fun formatearInstrucciones(instrucciones: String?): String {
        if (instrucciones.isNullOrEmpty()) return "No hay instrucciones disponibles"
        
        // Si ya viene formateado con números, devolverlo tal como está
        if (instrucciones.matches(Regex(".*\\d+\\.-.*"))) {
            return instrucciones
        }
        
        // Si no, intentar formatear split por puntos
        val pasos = instrucciones.split(".").filter { it.trim().isNotEmpty() }
        
        return if (pasos.size > 1) {
            pasos.mapIndexed { index, paso ->
                "${index + 1}.- ${paso.trim()}"
            }.joinToString("\n\n")
        } else {
            instrucciones
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(recetaId: Int): DetalleRecetaFragment {
            val fragment = DetalleRecetaFragment()
            val args = Bundle().apply {
                putInt("receta_id", recetaId)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
