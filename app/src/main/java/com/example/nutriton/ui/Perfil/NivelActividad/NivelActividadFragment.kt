package com.example.nutriton.ui.Perfil.NivelActividad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.nutriton.R
import com.example.nutriton.data.repository.NutritionRepository
import com.example.nutriton.ui.Preferencias.PreferenciasViewModel
import com.example.nutriton.ui.Preferencias.PreferenciasViewModelFactory

class NivelActividadFragment : Fragment() {
    
    private var nivelSeleccionado: String = "Moderado" // Por defecto

    private val viewModel: PreferenciasViewModel by viewModels {
        PreferenciasViewModelFactory(
            NutritionRepository.getInstance(requireContext())
        )
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nivelactividad, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupObservers()
        setupViews(view)
    }

    private fun setupObservers() {
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

        // Observar preferencias para establecer el nivel actual
        viewModel.preferencias.observe(viewLifecycleOwner) { preferencias ->
            nivelSeleccionado = preferencias.nivel_actividad
            updateSelectedLevel()
        }
    }

    private fun setupViews(view: View) {
        // Referencias a los layouts
        val layoutSedentario = view.findViewById<LinearLayout>(R.id.layout_sedentario)
        val layoutModerado = view.findViewById<LinearLayout>(R.id.layout_moderado)
        val layoutActivo = view.findViewById<LinearLayout>(R.id.layout_activo)
        val layoutMuyActivo = view.findViewById<LinearLayout>(R.id.layout_muy_activo)
        val btnGuardar = view.findViewById<Button>(R.id.btn_guardar_nivel)
        
        // Lista de todos los layouts para fácil manejo
        val todosLosLayouts = listOf(layoutSedentario, layoutModerado, layoutActivo, layoutMuyActivo)
        val nombresNiveles = listOf("Sedentario", "Moderado", "Activo", "Muy Activo")
        
        // Función para actualizar selección
        fun actualizarSeleccion(layoutSeleccionado: LinearLayout, nivel: String) {
            // Quitar selección de todos
            todosLosLayouts.forEach { layout ->
                layout.setBackgroundResource(R.drawable.nivel_actividad_normal)
            }
            
            // Agregar selección al clickeado
            layoutSeleccionado.setBackgroundResource(R.drawable.nivel_actividad_seleccionado)
            nivelSeleccionado = nivel
        }
        
        // Listeners para cada opción
        layoutSedentario.setOnClickListener { 
            actualizarSeleccion(layoutSedentario, "Sedentario") 
        }
        
        layoutModerado.setOnClickListener { 
            actualizarSeleccion(layoutModerado, "Moderado") 
        }
        
        layoutActivo.setOnClickListener { 
            actualizarSeleccion(layoutActivo, "Activo") 
        }
        
        layoutMuyActivo.setOnClickListener { 
            actualizarSeleccion(layoutMuyActivo, "Muy Activo") 
        }          // Botón guardar
        btnGuardar.setOnClickListener {
            // Actualizar el nivel en el ViewModel
            viewModel.actualizarNivelActividad(nivelSeleccionado)
            viewModel.guardarPreferencias()
            
            // Log para desarrollo (remover en producción)
            println("=== NIVEL DE ACTIVIDAD SELECCIONADO ===")
            println("Nivel: $nivelSeleccionado")
            println("====================================")
        }
    }

    private fun updateSelectedLevel() {
        // Actualizar la UI según el nivel seleccionado desde preferencias
        view?.let { v ->
            val layoutSedentario = v.findViewById<LinearLayout>(R.id.layout_sedentario)
            val layoutModerado = v.findViewById<LinearLayout>(R.id.layout_moderado)
            val layoutActivo = v.findViewById<LinearLayout>(R.id.layout_activo)
            val layoutMuyActivo = v.findViewById<LinearLayout>(R.id.layout_muy_activo)
            
            val todosLosLayouts = listOf(layoutSedentario, layoutModerado, layoutActivo, layoutMuyActivo)
            
            // Quitar selección de todos
            todosLosLayouts.forEach { layout ->
                layout.setBackgroundResource(R.drawable.nivel_actividad_normal)
            }
            
            // Seleccionar el nivel actual
            val layoutSeleccionado = when (nivelSeleccionado) {
                "Sedentario" -> layoutSedentario
                "Moderado" -> layoutModerado
                "Activo" -> layoutActivo
                "Muy Activo" -> layoutMuyActivo
                else -> layoutModerado
            }
            
            layoutSeleccionado.setBackgroundResource(R.drawable.nivel_actividad_seleccionado)
        }
    }
}
