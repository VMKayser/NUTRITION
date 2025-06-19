package com.example.nutriton.ui.Perfil.PreferenciasNutricion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.nutriton.R

class PreferenciasNutricionFragment : Fragment() {
    
    // Referencias a los elementos de la UI - Preparado para backend
    private lateinit var spinnerPlanDieta: Spinner
    private lateinit var etProteinas: EditText
    private lateinit var etCarbohidratos: EditText
    private lateinit var etGrasas: EditText
    private lateinit var etAlergias: EditText
    private lateinit var etAlimentosFavoritos: EditText
    private lateinit var btnGuardarPreferencias: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferenciasnutricion, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Inicializar referencias a las vistas
        initViews(view)
        
        // Configurar el spinner de plan de dieta
        setupPlanDietaSpinner()
        
        // Configurar el botón de guardar
        setupGuardarButton()
    }

    private fun initViews(view: View) {
        spinnerPlanDieta = view.findViewById(R.id.spinner_plan_dieta)
        etProteinas = view.findViewById(R.id.et_proteinas)
        etCarbohidratos = view.findViewById(R.id.et_carbohidratos)
        etGrasas = view.findViewById(R.id.et_grasas)
        etAlergias = view.findViewById(R.id.et_alergias)
        etAlimentosFavoritos = view.findViewById(R.id.et_alimentos_favoritos)
        btnGuardarPreferencias = view.findViewById(R.id.btn_guardar_preferencias)
    }

    private fun setupPlanDietaSpinner() {
        // Opciones para el spinner de plan de dieta
        val planesDieta = arrayOf(
            "Seleccionar plan de dieta",
            "Dieta Equilibrada",
            "Dieta Mediterránea", 
            "Dieta Vegana",
            "Dieta Vegetariana",
            "Dieta Keto",
            "Dieta Paleo",
            "Dieta sin Gluten"
        )
        
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            planesDieta
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPlanDieta.adapter = adapter
    }

    private fun setupGuardarButton() {
        btnGuardarPreferencias.setOnClickListener {
            // Obtener los datos de los campos
            val planDietaSeleccionado = spinnerPlanDieta.selectedItem.toString()
            val proteinas = etProteinas.text.toString()
            val carbohidratos = etCarbohidratos.text.toString()
            val grasas = etGrasas.text.toString()
            val alergias = etAlergias.text.toString()
            val alimentosFavoritos = etAlimentosFavoritos.text.toString()
            
            // TODO: Aquí se enviará la información al backend
            // Log de los datos para desarrollo (remover en producción)
            println("=== PREFERENCIAS DE NUTRICIÓN ===")
            println("Plan de Dieta: $planDietaSeleccionado")
            println("Proteínas: $proteinas%")
            println("Carbohidratos: $carbohidratos%") 
            println("Grasas: $grasas%")
            println("Alergias: $alergias")
            println("Alimentos Favoritos: $alimentosFavoritos")
            println("===========================")
        }
    }
}
