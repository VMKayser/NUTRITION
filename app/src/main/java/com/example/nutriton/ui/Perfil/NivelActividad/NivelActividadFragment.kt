package com.example.nutriton.ui.Perfil.NivelActividad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.nutriton.R

class NivelActividadFragment : Fragment() {
    
    private var nivelSeleccionado: String = "Moderado" // Por defecto
    
    override fun onCreateView(
        inflater: LayoutInflater, 
        container: ViewGroup?, 
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nivelactividad, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
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
        }
          // Botón guardar
        btnGuardar.setOnClickListener {
            // TODO: Aquí se guardará el nivel en el backend
            // Log para desarrollo (remover en producción)
            println("=== NIVEL DE ACTIVIDAD SELECCIONADO ===")
            println("Nivel: $nivelSeleccionado")
            println("====================================")
            
            // TODO: Implementar navegación cuando esté lista
            // findNavController().popBackStack()
        }
    }
}
